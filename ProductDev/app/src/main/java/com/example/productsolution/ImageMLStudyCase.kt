package com.example.productsolution

import android.annotation.SuppressLint
import android.content.Intent
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
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher


class ImageMLStudyCase : AppCompatActivity() {
    private lateinit var binding : ActivityImageMlstudyCaseBinding
    private lateinit var takePictureLauncher : ActivityResultLauncher<Intent>
    override fun onStart() {
        super.onStart()
        defineTakePictureLauncher()
    }
    override fun onCreate(savedInstanceState: Bundle?) {


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

    }

    private fun uploadImageHandler()
    {

    }

    private fun takeImageHandler()
    {
        takePictureLauncher.launch(Intent(baseContext, CameraXActivity::class.java))

    }

    private fun predictHandler()
    {

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

    private fun defineTakePictureLauncher()
    {
        takePictureLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { result ->
            val data : Intent? = result.data

            Toast.makeText(baseContext, data.toString() + " " + result.resultCode.toString(), Toast.LENGTH_SHORT).show()
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
        binding.ivContent.setImageBitmap(bitmap)
    }
    private fun provideContent()  : ArrayList<Int>
    {
        // Return list of images
        return arrayListOf(R.drawable.apple_braeburn_0, R.drawable.apple_granny_smith_0,R.drawable.apricot_0)
    }
}