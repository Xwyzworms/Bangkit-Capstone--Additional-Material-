package com.example.productsolution.imageStudyCases

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.productsolution.databinding.ActivityMainMlapiBinding

class MainMLApiActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainMlapiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainMlapiBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}