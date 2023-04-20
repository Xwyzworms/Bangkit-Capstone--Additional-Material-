/*
#       Written by : Rose (Pratama Azmi A)
#       Date : Unknown 
#       Text editor : AndroidStudio + VIM
*/
package com.example.productsolution.imageStudyCases.imageClassifierUtils

import android.content.Context
import android.content.res.AssetManager
import android.util.Size
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.gpu.CompatibilityList
import org.tensorflow.lite.gpu.GpuDelegate
import org.tensorflow.lite.nnapi.NnApiDelegate
import java.io.File
import android.graphics.Bitmap
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.TensorProcessor
import org.tensorflow.lite.support.common.ops.DequantizeOp
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.common.ops.QuantizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.InputStream

class ImageClassifierRaw(private var context : Context,
                         private var modelPath : String,
                         private var labelPath : String,
                         private var threshold : Float,
                         private var delegate : Int,
                         private var isQuantizedModel : Boolean,
                         private var inputSize : Size,
                         private var outputSize : IntArray
                                )
{
    /*
    *  TODO
    *   1. Define Interpreter & Its options
    *   2. Define Module to read Label
    *   3. Define Module to read model tensorflow
    *   4. Define Converter bitmap to byteBuffer ( Or not )
    *   5. Do prediction
    *   6. Mapped the output to correct labels
    * */
    private lateinit var interpreter : Interpreter
    private lateinit var predictionsBuffer : TensorBuffer
    private lateinit var associatedLabels : List<String>
    private lateinit var imageProcessor : ImageProcessor
    private var assetManager : AssetManager = context.assets
    private val interpreterOptions : Interpreter.Options = Interpreter.Options()
    init {

        setupLabel()
        setupInterpreterOps()
        setupModel()
    }

    private fun prepareGpuDelegate() : GpuDelegate
    {

        val gpuDelegateOptions : GpuDelegate.Options = GpuDelegate.Options().setInferencePreference(GpuDelegate.Options.INFERENCE_PREFERENCE_SUSTAINED_SPEED)
        val gpuDelegate : GpuDelegate = GpuDelegate(gpuDelegateOptions)
        return gpuDelegate
    }

    private fun prepareNNApiDelegate() : NnApiDelegate
    {
        val nnApiDelegateOptions : NnApiDelegate.Options = NnApiDelegate.Options()
        nnApiDelegateOptions.executionPreference = NnApiDelegate.Options.EXECUTION_PREFERENCE_SUSTAINED_SPEED
        nnApiDelegateOptions.useNnapiCpu = true
        val nnApiDelegate : NnApiDelegate = NnApiDelegate(nnApiDelegateOptions)
        return nnApiDelegate
    }
    private fun setupInterpreterOps()
    {
        when(delegate)
        {
            DELEGATE_CPU ->
            {
                // Do nothing
            }

            DELEGATE_GPU ->
            {
                if(CompatibilityList().isDelegateSupportedOnThisDevice)
                {
                    interpreterOptions.addDelegate(prepareGpuDelegate())
                }
                else
                {
                    interpreterOptions.addDelegate(prepareNNApiDelegate())
                }
            }

            DELEGATE_NNAPI ->
            {
                interpreterOptions.addDelegate(prepareNNApiDelegate())
            }

        }
    }

    fun setupPredictionBuffer()
    {
        predictionsBuffer = if(isQuantizedModel)
        {
            TensorBuffer.createFixedSize(outputSize, DataType.UINT8)
        }
        else
        {
            TensorBuffer.createFixedSize(outputSize, DataType.FLOAT32)
        }
    }
    fun setupImageProcessor()
    {
        if(isQuantizedModel)
        {
            imageProcessor = ImageProcessor.Builder()
                .add(ResizeOp(inputSize.height, inputSize.width, ResizeOp.ResizeMethod.BILINEAR))
                .add(NormalizeOp(0f, 255f))
                .add(QuantizeOp(999f,999f))
                .build()
        }
        else
        {
            imageProcessor = ImageProcessor.Builder()
                .add(ResizeOp(inputSize.height, inputSize.width, ResizeOp.ResizeMethod.BILINEAR))
                .add(NormalizeOp(0f,255f)).build()
        }

    }
    private fun handlerPredictionBuffer() : List<DefaultCategoryImageClassification>
    {
        val predictionResults = predictionsBuffer.floatArray
        // TODO later
        /*
        * 1. Get resutls from predictions
        * 2. Applied the associatedLabels for each predictions
        * 3. return as List<Default...>
        * */
        return arrayListOf()
    }
    fun classifyImage(bitmap : Bitmap) : List<DefaultCategoryImageClassification>
    {
        setupImageProcessor()
        setupPredictionBuffer()
        val inputTensorImage : TensorImage = TensorImage(DataType.FLOAT32)
        inputTensorImage.load(bitmap)

        if(isQuantizedModel)
        {
            val tensorProcessor = TensorProcessor.Builder()
                .add(DequantizeOp(999f, 999f))
                .build()

            predictionsBuffer = tensorProcessor.process(predictionsBuffer)
        }else
        {
            interpreter.run(inputTensorImage.buffer, predictionsBuffer)
        }

        val arrayOfResults : List<DefaultCategoryImageClassification> = handlerPredictionBuffer()
        return arrayOfResults
    }
    private fun setupModel()
    {
        interpreter = Interpreter(File(modelPath), interpreterOptions)
    }

    private fun setupLabel()
    {
        val inputStream : InputStream = assetManager.open(labelPath)
        val labelContents : List<String> = inputStream.bufferedReader().useLines {
            it.toList()
        }
        associatedLabels = labelContents
    }

    companion object {
        const val DELEGATE_CPU = 0 ;
        const val DELEGATE_GPU = 1;
        const val DELEGATE_NNAPI = 2;
    }
}