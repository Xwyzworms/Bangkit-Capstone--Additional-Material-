package com.example.productsolution

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.productsolution.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()
    }

    private fun setupRecyclerView()
    {
        binding.rvMain.layoutManager = LinearLayoutManager(baseContext)
        binding.rvMain.adapter = MainActivityAdapter(provideData())
        {
            var intent :Intent? = null
            when(it)
            {
                provideData()[0] ->
                {
                    intent = Intent(baseContext, ImageMLStudyCase::class.java)
                }
                provideData()[1] ->
                {
                    intent = Intent(baseContext, TextMLStudyCase::class.java)
                }
            }
            startActivity(intent)
        }
    }

    private fun provideData() : ArrayList<String>
    {
        return arrayListOf("ImageStudyCase","TextStudyCase")

    }
}