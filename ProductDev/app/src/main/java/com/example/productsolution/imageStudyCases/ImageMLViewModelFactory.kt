/*
#       Written by : Rose (Pratama Azmi A)
#       Date : Unknown 
#       Text editor : AndroidStudio + VIM
*/
package com.example.productsolution.imageStudyCases

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.productsolution.generalUtils.MainRepository

class ImageMLViewModelFactory(private val mainRepository: MainRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(MainMLApiViewModel::class.java) -> {
                return MainMLApiViewModel(mainRepository) as T
            }
        }
        throw IllegalArgumentException("No Class Defined")

    }
}