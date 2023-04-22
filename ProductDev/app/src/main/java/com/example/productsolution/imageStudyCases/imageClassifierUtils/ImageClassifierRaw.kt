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
import android.graphics.Bitmap
import android.util.Log
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.TensorProcessor
import org.tensorflow.lite.support.common.ops.CastOp
import org.tensorflow.lite.support.common.ops.DequantizeOp
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.common.ops.QuantizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.FileInputStream
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel
import java.util.*
import kotlin.math.max

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
        interpreterOptions.setNumThreads(5)
    }


    fun setupImageProcessor()
    {
        imageProcessor = if(isQuantizedModel) {
            ImageProcessor.Builder()
                .add(ResizeOp(inputSize.height, inputSize.width, ResizeOp.ResizeMethod.BILINEAR))
                .add(NormalizeOp(0f, 255f))
                .add(QuantizeOp(mobileNet_InputZeropoints, mobileNet_InputScales))
                .add(CastOp(DataType.UINT8))
                .build()

        } else {
            ImageProcessor.Builder()
                .add(ResizeOp(inputSize.height, inputSize.width, ResizeOp.ResizeMethod.BILINEAR))
                .add(NormalizeOp(0f,255f))
                .add(CastOp(DataType.FLOAT32))
                .build()
        }

    }
    private fun handlerPredictionBuffer(predBuffer: FloatArray) : DefaultCategoryImageClassification {

        // Later, i need to understand a bc d e f g first
        //
        // TODO later
        /*
        * 1. Get resutls from predictions
        * 2. Applied the associatedLabels for each predictions
        * 3. return the max
        * */

        var maxDefault : DefaultCategoryImageClassification = DefaultCategoryImageClassification("-",0f,0)

        for(pred in predBuffer.indices)
        {
            if(predBuffer[pred] > maxDefault.score)
            {
                maxDefault =  parseToDefault(pred, predBuffer[pred])
            }
        }

        return maxDefault
    }

    private fun parseToDefault(contentIndx : Int, contentScore : Float) : DefaultCategoryImageClassification
    {
        val modifiedContent : DefaultCategoryImageClassification =
            DefaultCategoryImageClassification(associatedLabels[contentIndx],if (contentScore > 100f)
            contentScore else contentScore,contentIndx)
        return modifiedContent
    }

    private fun scaledBitmap(bitmap: Bitmap): ByteBuffer {
        val scaledBitmap: Bitmap =
            Bitmap.createScaledBitmap(bitmap, inputSize.width, inputSize.height, false)
        return convertToByteBuffer(scaledBitmap)
    }

    private fun convertToByteBuffer(bitmap: Bitmap) : ByteBuffer
    {
        val byteBuffer : ByteBuffer = ByteBuffer.allocateDirect( inputSize.width* inputSize.height * 3)
        byteBuffer.order(ByteOrder.nativeOrder())

        val intValues = IntArray(inputSize.height * inputSize.width)

        bitmap.getPixels(intValues, 0,bitmap.width, 0 , 0 ,bitmap.width, bitmap.height)
        var pixel = 0
        for (i in 0 until inputSize.width) {
            for (j in 0 until inputSize.height) {
                val input = intValues[pixel++]

                byteBuffer.put(((input shr 16) and 0xFF).toByte())
                byteBuffer.put(((input shr 8) and 0xFF).toByte())
                byteBuffer.put((input and 0xFF).toByte())
            }
        }
        return byteBuffer
    }
    fun classifyImage(bitmap: Bitmap): DefaultCategoryImageClassification {
        setupImageProcessor()
        var tensorImage =
            if (isQuantizedModel) TensorImage(DataType.UINT8) else TensorImage(DataType.FLOAT32)

        tensorImage.load(bitmap)

        tensorImage = imageProcessor.process(tensorImage)

        var predictionBuffer = TensorBuffer.createFixedSize(outputSize, DataType.UINT8)

        interpreter.run(tensorImage.buffer, predictionBuffer.buffer)
        return handlerPredictionBuffer(predictionBuffer.floatArray)
    }
    private fun setupModel()
    {
        val fileDescriptor = assetManager.openFd(modelPath)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        interpreter = Interpreter(fileChannel.map(FileChannel.MapMode.READ_ONLY, fileDescriptor.startOffset, fileDescriptor.declaredLength),interpreterOptions)

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

        const val mobileNet_InputScales : Float = 0.0078125f
        const val mobileNet_InputZeropoints : Float = 128f

        const val mobileNet_OutputScales : Float = 0.00390625f
        const val mobileNet_OutputZeropoints : Float = 0f
    }
}