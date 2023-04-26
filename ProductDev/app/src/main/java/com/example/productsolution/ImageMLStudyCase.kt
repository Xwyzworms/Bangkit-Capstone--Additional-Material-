package com.example.productsolution

import android.content.Intent
import android.content.res.AssetManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.productsolution.databinding.ActivityImageMlstudyCaseBinding
import com.example.productsolution.imageStudyCases.CameraXActivity
import com.example.productsolution.imageStudyCases.ImageMLMainAdapter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.util.Size
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.core.graphics.drawable.toBitmap
import com.example.productsolution.imageStudyCases.MainMLApiActivity
import com.example.productsolution.imageStudyCases.imageClassifierUtils.ImageClassifierRaw
import com.example.productsolution.imageStudyCases.imageClassifierUtils.ImageClassifierModelTaskVision
import java.io.InputStream


class ImageMLStudyCase : AppCompatActivity() {
    private lateinit var binding : ActivityImageMlstudyCaseBinding
    private lateinit var takePictureLauncher : ActivityResultLauncher<Intent>
    private lateinit var uploadPictureLauncher : ActivityResultLauncher<String>
    override fun onStart() {
        super.onStart()
        defineTakePictureLauncher()
        defineUploadImageLauncher()
    }
    override fun onCreate(savedInstanceState: Bundle?)
    {

        super.onCreate(savedInstanceState)
        binding = ActivityImageMlstudyCaseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()
        setupButtonListener()
    }

    private fun setupButtonListener()
    {
        binding.btnUploadImage.setOnClickListener {
            uploadImageHandler()
        }

        binding.btnTakeImage.setOnClickListener {
            takeImageHandler()
        }

        binding.btnPredict.setOnClickListener {
            predictHandler()
        }

        binding.btnApiTest.setOnClickListener {
            val intent : Intent = Intent(this, MainMLApiActivity::class.java)
            startActivity(intent)

        }

    }

    private fun uploadImageHandler()
    {
        uploadPictureLauncher.launch("image/*")
    }

    private fun takeImageHandler()
    {
        takePictureLauncher.launch(Intent(baseContext, CameraXActivity::class.java))

    }

    private fun predictHandler()
    {
        val am : AssetManager = this.assets
        val textInputStream : InputStream = am.open(DEFAULT_LABEL_TEST)

        //val imgModelTaskVision = ImageClassifierModelTaskVision(DEFAULT_THRESHOLD, DEFAULT_THREADS,
        //    DEFAULT_MAXRESULTS, DEFAULT_LABEL_TEST ,DEFAULT_MODEL_TEST, this, DEFAULT_DELEGATE)

        val imgModelRaw : ImageClassifierRaw = ImageClassifierRaw(this,
            DEFAULT_MODEL_TEST, DEFAULT_LABEL_TEST, DEFAULT_THRESHOLD,
            DEFAULT_DELEGATE, DEFAULT_QUANTIZED, DEFAULT_INPUT_SIZE,
            DEFAULT_OUTPUT_SIZE
            )
        val result = imgModelRaw.classifyImage(binding.ivContent.drawable.toBitmap())
        //val result = imgModelTaskVision.classify(binding.ivContent.drawable.toBitmap(), (getSystemService(
        //    WINDOW_SERVICE) as WindowManager).defaultDisplay.rotation)
        Toast.makeText(this, result.toString(), Toast.LENGTH_SHORT).show()
    }

    private fun setupRecyclerView()
    {
        binding.rvImageMain.layoutManager = LinearLayoutManager(baseContext, LinearLayoutManager.HORIZONTAL, false)
        binding.rvImageMain.adapter = ImageMLMainAdapter(baseContext, provideContent())
        {
            when(it)
            {
                R.drawable.apple_braeburn_0 ->
                {
                    binding.ivContent.setImageDrawable(AppCompatResources.getDrawable(baseContext,R.drawable.apple_braeburn_0))
                }
                R.drawable.apple_granny_smith_0 ->
                {
                    binding.ivContent.setImageDrawable(AppCompatResources.getDrawable(baseContext,R.drawable.apple_granny_smith_0))
                }
                R.drawable.apricot_0 ->
                {
                    binding.ivContent.setImageDrawable(AppCompatResources.getDrawable(baseContext,R.drawable.apricot_0))
                }

            }
        }
    }

    private fun defineUploadImageLauncher()
    {
        uploadPictureLauncher = registerForActivityResult(ActivityResultContracts.GetContent())
        {result ->
            val data : Uri? = result

            if(data != null )
            {
                val fullPhotoUri : Uri? = data
                if(fullPhotoUri != null )
                {
                    val bitmap : Bitmap? = uriToBitmap(fullPhotoUri)
                    if(bitmap != null)
                    {
                        binding.ivContent.setImageBitmap(bitmap)
                    }
                }
            }

        }
    }

    private fun uriToBitmap(uri : Uri) : Bitmap?
    {
        val input : InputStream? = contentResolver.openInputStream(uri)
        val bitmapFactoryOptions : BitmapFactory.Options  = BitmapFactory.Options().apply {
            inJustDecodeBounds = true // Bitmap disimpan di out, cuman extract metadata ( Height, size , dll ) but ga load fullimage ke memory
            inPreferredConfig = Bitmap.Config.ARGB_8888
        }
        BitmapFactory.decodeStream(input, null, bitmapFactoryOptions)
        input?.close()
        if((bitmapFactoryOptions.outHeight == -1) || (bitmapFactoryOptions.outWidth == -1)) return null

        val originalSize : Int = if (bitmapFactoryOptions.outHeight > bitmapFactoryOptions.outWidth) bitmapFactoryOptions.outHeight else bitmapFactoryOptions.outWidth
        val ratio : Double = if (originalSize > binding.ivContent.maxWidth) (originalSize / binding.ivContent.maxWidth).toDouble() else 1.0
        val bitmapOps : BitmapFactory.Options = BitmapFactory.Options().apply {
            inSampleSize = getPowerTwoForSampleRatio(ratio)
            inPreferredConfig = Bitmap.Config.ARGB_8888
        }

        val inputStream : InputStream? = contentResolver.openInputStream(uri)
        val bitmap : Bitmap? = BitmapFactory.decodeStream(inputStream, null, bitmapOps)
        inputStream?.close()
        return bitmap

    }
    private fun getPowerTwoForSampleRatio(ratio : Double) : Int
    {
        val result = Integer.highestOneBit(Math.floor(ratio).toInt()) // Get the left most bit ( Basically untuk approximation )
        if (result == 0) return 1
        return result
    }
    private fun defineTakePictureLauncher()
    {
        takePictureLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { result ->
            val data : Intent? = result.data


            if(data != null && result.resultCode == CameraXActivity.CAMERAX_RESULT_CODE)
            {
                val byteArray = data.getByteArrayExtra("CODE")
                if(byteArray != null)
                {
                    setupImageContentFromResult(byteArray)
                }
            }
            else
            {
                Toast.makeText(baseContext, data.toString() + " " + result.resultCode.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun setupImageContentFromResult(result : ByteArray) : Unit
    {
        val bitmap = BitmapFactory.decodeByteArray(result, 0, result.size)
        binding.ivContent.setImageBitmap(rotateImage((bitmap)))
    }
    private fun rotateImage(bitmap: Bitmap) : Bitmap
    {
        val mat : Matrix = Matrix()
        mat.postRotate(90f)
        return Bitmap.createBitmap(bitmap, 0, 0 , bitmap.width, bitmap.height, mat, false)
    }
    private fun provideContent()  : ArrayList<Int>
    {
        // Return list of images
        return arrayListOf(R.drawable.apple_braeburn_0, R.drawable.apple_granny_smith_0,R.drawable.apricot_0)
    }

    companion object {
        const val DEFAULT_THRESHOLD : Float = 0.5f
        const val DEFAULT_THREADS : Int = 2
        const val DEFAULT_MAXRESULTS : Int = 1
        const val DEFAULT_DELEGATE : Int = 0
        val DEFAULT_INPUT_SIZE : Size = Size(224, 224)
        val DEFAULT_OUTPUT_SIZE : IntArray = intArrayOf(1, 1001)
        const val DEFAULT_QUANTIZED : Boolean = true
        const val DEFAULT_LABEL_TEST : String = "labels_mobilenet_quant_v1_224.txt"
        const val DEFAULT_MODEL_TEST : String = "mobilenet_v1_1.0_224_quant.tflite"
    }
}