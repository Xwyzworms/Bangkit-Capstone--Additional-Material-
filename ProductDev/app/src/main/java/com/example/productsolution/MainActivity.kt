package com.example.productsolution

import android.app.PendingIntent.OnFinished
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.productsolution.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkPermission()
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

    private fun allPermissionGranted() : Boolean {
        return REQUIRED_PERMISSION.all {
            ActivityCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
        }
    }
    private fun checkPermission ()
    {
        if(allPermissionGranted())
        {
            Toast.makeText(baseContext, "Semua permissoin tleasdfhuiaosdfhuasdf", Toast.LENGTH_SHORT).show()
        }
        else
        {
            askPermission()
        }
    }
    private fun askPermission ()
    {
        ActivityCompat.requestPermissions(this, REQUIRED_PERMISSION, 10)
    }
    private fun provideData() : ArrayList<String>
    {
        return arrayListOf("ImageStudyCase","TextStudyCase")
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 10)
        {
            if(allPermissionGranted())
            {
                Toast.makeText(baseContext, "Semua permissoin tleasdfhuiaosdfhuasdf", Toast.LENGTH_SHORT).show()
            }
            else
            {
                finish()
            }
        }
    }

    companion object {
        private val REQUIRED_PERMISSION : Array<String> = mutableListOf(
            android.Manifest.permission.CAMERA
        ).toTypedArray()
    }
}