package com.example.productsolution.imageStudyCases

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.productsolution.databinding.ActivityCameraXactivityBinding

class CameraXActivity : AppCompatActivity() {
    private lateinit var binding : ActivityCameraXactivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraXactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}