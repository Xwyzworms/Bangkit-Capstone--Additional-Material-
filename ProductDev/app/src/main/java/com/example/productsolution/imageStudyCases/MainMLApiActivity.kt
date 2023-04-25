package com.example.productsolution.imageStudyCases

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.productsolution.databinding.ActivityMainMlapiBinding
import com.example.productsolution.generalUtils.MainRepository

class MainMLApiActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainMlapiBinding
    private lateinit var factory : ViewModelProvider.Factory
    private lateinit var viewModel : MainMLApiViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainMlapiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupMainViewModel()
        setupBtnHandler()

    }
    private fun setupBtnHandler()
    {
        binding.btnApiAddfruit.setOnClickListener {
            addFruitHandler()
        }


        binding.btnApiGetFruits.setOnClickListener {
            getFruitHandler()
        }

        binding.btnApiGetFruits.setOnClickListener {
            deleteFruitHandler()
        }
    }
    private fun addFruitHandler() {
        // TODO later
    }

    private fun getFruitHandler() {
        viewModel.getAllFruits()
    }
    private fun deleteFruitHandler() {
        viewModel.deleteAllFruits()
    }

    private fun setupMainViewModel() {
        factory = ImageMLViewModelFactory(MainRepository())
        viewModel = ViewModelProvider(this, factory).get(MainMLApiViewModel::class.java)
    }
}