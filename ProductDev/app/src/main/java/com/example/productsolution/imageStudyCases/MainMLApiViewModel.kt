/*
#       Written by : Rose (Pratama Azmi A)
#       Date : Unknown 
#       Text editor : AndroidStudio + VIM
*/
package com.example.productsolution.imageStudyCases

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.productsolution.generalUtils.Fruit
import com.example.productsolution.generalUtils.FruitResponse
import com.example.productsolution.generalUtils.MainRepository
import com.example.productsolution.generalUtils.NetworkClass

class MainMLApiViewModel(private val mainRepository: MainRepository) : ViewModel()
{
    fun getAllFruits() : LiveData<NetworkClass<FruitResponse>>
    {
        return mainRepository.getFruits()
    }

    fun deleteAllFruits() : LiveData<NetworkClass<FruitResponse>>  {
        return mainRepository.deleteAllFruits()
    }

    fun createNewFruit(fruit : Fruit) : LiveData<NetworkClass<FruitResponse>>
    {
        return mainRepository.createNewFruit(fruit)
    }
}