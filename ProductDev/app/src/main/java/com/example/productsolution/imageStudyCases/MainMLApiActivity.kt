package com.example.productsolution.imageStudyCases

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.productsolution.databinding.ActivityMainMlapiBinding
import com.example.productsolution.generalUtils.MainRepository
import com.example.productsolution.generalUtils.NetworkClass

class MainMLApiActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainMlapiBinding
    private lateinit var factory : ImageMLViewModelFactory
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
            Toast.makeText(this, "Bro", Toast.LENGTH_SHORT).show()
            getFruitHandler()
        }

    }
    private fun addFruitHandler() {
        // TODO later
    }

    private fun getFruitHandler() {
        Log.d("BROO", "ANJAY")
        viewModel.getAllFruits().observe(this) {
            when (it) {
                is NetworkClass.Success -> {
                    Toast.makeText(this, it.data.toString(), Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun deleteFruitHandler() {
        viewModel.deleteAllFruits()
    }

    private fun setupMainViewModel() {
        factory = ImageMLViewModelFactory(MainRepository())
        viewModel = ViewModelProvider(this, factory).get(MainMLApiViewModel::class.java)
    }
}