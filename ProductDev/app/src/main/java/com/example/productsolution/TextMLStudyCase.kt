package com.example.productsolution

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.productsolution.databinding.ActivityTextMlstudyCaseBinding

class TextMLStudyCase : AppCompatActivity() {

    private lateinit var binding : ActivityTextMlstudyCaseBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTextMlstudyCaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    private fun setupOnClickListener()
    {
        binding.btnPredict.setOnClickListener {
            doPredict()
        }
    }

    private fun doPredict()
    {

    }
}