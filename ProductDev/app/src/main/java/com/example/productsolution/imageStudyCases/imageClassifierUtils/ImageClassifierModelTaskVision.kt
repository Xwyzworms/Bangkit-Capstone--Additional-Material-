/*
#       Written by : Rose (Pratama Azmi A)
#       Date : Unknown 
#       Text editor : AndroidStudio + VIM
*/
package com.example.productsolution.imageStudyCases.imageClassifierUtils

import android.content.Context
import android.content.res.AssetManager
import org.tensorflow.lite.gpu.CompatibilityList
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.vision.classifier.ImageClassifier
import org.tensorflow.lite.task.vision.classifier.ImageClassifier.ImageClassifierOptions
import org.tensorflow.lite.task.vision.classifier.ImageClassifier.createFromFileAndOptions
import android.graphics.Bitmap
import android.renderscript.ScriptGroup.Input
import android.view.Surface
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.core.vision.ImageProcessingOptions
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.io.File
import java.io.FileInputStream
import java.io.InputStream


/*
* TODO
*  1. Define parameters to the model
*  2. Setup the model
*  3. Do prediction for input image
*
* */
class ImageClassifierModelTaskVision (
    private var threshold : Float = 0.5f,
    private var numThreads : Int = 2,
    private var maxResults : Int = 1,
    private var correspondingLabels : String,
    private var correspondingModels : String,
    private val context : Context,
    private val currentDelegate : Int = 0
        )
{
    private lateinit var imageModelClassifier : ImageClassifier
    private lateinit var associatedLabels : List<String>
    init {
        setupImageClassifier()
    }

    fun classify(image : Bitmap, rotation : Int) : List<DefaultCategoryImageClassification>
    {
        val tensorImage : TensorImage = TensorImage.fromBitmap(image)
        // For preprocessing and post processing input image
        val imageProcessor : ImageProcessor = ImageProcessor.Builder().build()

        imageProcessor.process(tensorImage)
        val imageProcessingOptions  : ImageProcessingOptions = ImageProcessingOptions.builder().setOrientation(getOrientationFromRotation(rotation))
            .build()

        val result = imageModelClassifier.classify(tensorImage, imageProcessingOptions)

        return parseOutputs(result)
    }
    private fun parseOutputs(listClassification : List<Classifications>) : List<DefaultCategoryImageClassification>
    {
        val mutableParseOutputs : MutableList<DefaultCategoryImageClassification> = mutableListOf()
        for(classification in listClassification)
        {
            for(cat in classification.categories)
            {
                mutableParseOutputs.add(
                    DefaultCategoryImageClassification(
                        associatedLabels.get(cat.index),
                        cat.score,
                        cat.index)
                )

            }
        }
        return mutableParseOutputs
    }
    private fun setupLabels()
    {
        val assetManager : AssetManager = context.assets


        associatedLabels = FileUtil.loadLabels(assetManager.open(correspondingLabels))
    }
    private fun setupImageClassifier()
    {
        setupLabels()
        val imageClassifierOptions = ImageClassifierOptions.builder()
            .setScoreThreshold(threshold)
            .setMaxResults(maxResults)

        val baseOptionBuilder : BaseOptions = BaseOptions.builder()
            .setNumThreads(numThreads).also {
                when(currentDelegate)
                {
                    DELEGATE_CPU -> {
                        // Do nothing
                    }
                    DELEGATE_GPU -> {
                        if(CompatibilityList().isDelegateSupportedOnThisDevice) it.useGpu()
                        else it.useNnapi()
                    }
                    DELEGATE_NNAPI -> {
                        it.useNnapi()
                    }
                }
            }.build()

        imageClassifierOptions.setBaseOptions(baseOptionBuilder)

        // Create the model
        try {
            imageModelClassifier = createFromFileAndOptions(context, correspondingModels, imageClassifierOptions.build())
        }
        catch (e : IllegalStateException)
        {

        }

    }

    private fun getOrientationFromRotation(rotation: Int) : ImageProcessingOptions.Orientation
    {
        // TODO
        // http://sylvana.net/jpegcrop/exif_orientation.html <-- System yang dipake
        // Get orientation from PreviewView
        //
        // Return the Exact ImageProcessingOrientations Options

        when (rotation)
        {
            Surface.ROTATION_270 -> {
                return ImageProcessingOptions.Orientation.BOTTOM_RIGHT
            }
            Surface.ROTATION_180 -> {
                return ImageProcessingOptions.Orientation.RIGHT_BOTTOM
            }
            Surface.ROTATION_90 -> {
                return ImageProcessingOptions.Orientation.TOP_LEFT
            }
            else -> {
                return ImageProcessingOptions.Orientation.RIGHT_TOP
            }
        }
    }

    companion object {
        const val DELEGATE_CPU = 0 ;
        const val DELEGATE_GPU = 1;
        const val DELEGATE_NNAPI = 2;

    }

}