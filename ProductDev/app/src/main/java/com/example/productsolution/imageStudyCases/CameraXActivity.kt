package com.example.productsolution.imageStudyCases

import android.content.Context
import android.graphics.BitmapFactory
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.example.productsolution.databinding.ActivityCameraXactivityBinding
import com.google.common.util.concurrent.ListenableFuture
import android.graphics.Bitmap
import android.media.Image
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.camera.core.*
import androidx.camera.core.ImageCapture.OnImageCapturedCallback
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer

class CameraXActivity : AppCompatActivity() {
    private lateinit var binding : ActivityCameraXactivityBinding
    private var imageCaptureUseCase : ImageCapture? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraXactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prepareUseCase()
        setupClickListener()
    }

    private fun setupClickListener()
    {
        binding.btnTakePict.setOnClickListener {
           takePicture()
        }
    }
    @androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
    private fun takePicture() : Unit
    {
        val imageCaptureUseCase = imageCaptureUseCase?:return

        // Just take the pict don t need to define the contentValues
        imageCaptureUseCase.takePicture(ContextCompat.getMainExecutor(baseContext), object : OnImageCapturedCallback(){
            override fun  onCaptureSuccess(image: ImageProxy) {
                val img : Image?= image.image
                if(img != null)
                {
                    val byteArray : ByteArray = prepareImageByteArray(
                        scaleBitmapDown(convertImageToBitmap(img),224))
                    //val byteArray : ByteArray = prepareImageByteArray(provideDummyBitmap())
                    sendToLauncher(byteArray)
                }
                else
                {
                    Toast.makeText(baseContext, "DUDE null njir",Toast.LENGTH_SHORT).show()
                }
                image.close()
            }
        })
    }
    private fun provideDummyBitmap() :Bitmap
    {
        return BitmapFactory.decodeResource(baseContext.resources, com.example.productsolution.R.drawable.apricot_0)
    }
    private fun convertImageToBitmap(image : Image) :Bitmap
    {
        val byteBuffer : ByteBuffer = image.planes[0].buffer

        val capacity : ByteArray = ByteArray(byteBuffer.capacity())

        byteBuffer.get(capacity) // Basically copy to capacity

        return BitmapFactory.decodeByteArray(capacity, 0, capacity.size, null)

    }
    private fun prepareImageByteArray(image : Bitmap) : ByteArray
    {
        val byteArrayOutputStream : ByteArrayOutputStream = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        //image.recycle()
        return byteArray
    }
    private fun sendToLauncher(byteArray: ByteArray)
    {
        val intent = Intent()
        intent.putExtra(CAMERAX_KEY, byteArray)
        setResult(CAMERAX_RESULT_CODE,intent)
        finish()
    }
    private fun prepareUseCase()
    {
        // Prepare cameraProviderFuture
        // Prepare cameraProvider
        // Initiate Preview use case, imageCapture use case
        // Bind it to lifeCycle

        val cameraProviderFuture : ListenableFuture<ProcessCameraProvider> = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({

            val cameraProvider :ProcessCameraProvider = cameraProviderFuture.get()

            // Define the previewUseCase
            val previewUseCase : Preview = Preview.Builder().build().also {
                it.setSurfaceProvider (binding.previewView.surfaceProvider)
            }

            imageCaptureUseCase = ImageCapture.Builder().build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            try
            {
                //Unbind all useCases, sebelum rebind
                cameraProvider.unbindAll()
                //Bind to lifecycler
                cameraProvider.bindToLifecycle(this, cameraSelector, previewUseCase, imageCaptureUseCase )

            }
            catch (e : Exception)
            {
                // Log shit
            }
        }
            , ContextCompat.getMainExecutor(this))

    }

    companion object {
        const val CAMERAX_RESULT_CODE : Int = 0
        const val CAMERAX_KEY : String ="CODE"
    }
}