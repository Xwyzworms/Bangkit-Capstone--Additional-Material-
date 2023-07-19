/*
#       Written by : Rose (Pratama Azmi A)
#       Date : Unknown 
#       Text editor : AndroidStudio + VIM
*/
package com.example.productsolution.generalUtils

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import retrofit2.Response

class MainRepository {
    fun getFruits() : LiveData<NetworkClass<FruitResponse>>
    {
        Log.d("BROO", "getFruits ada kok")
        val fruits : LiveData<NetworkClass<FruitResponse>> =  liveData(
            context = Dispatchers.IO,
            block = {
                try {
                    val response = RetrofitMain.getInstance().getFruits()
                    if(response.isSuccessful)
                    {
                        val apiContents : FruitResponse? = response.body()
                        if(apiContents != null)
                        {
                            emit(NetworkClass.Success(apiContents))
                        }
                    }
                    else
                    {
                        emit(NetworkClass.Error("Error", 404, ))
                    }
                }catch (e : Exception)
                {
                    emit(NetworkClass.Error("Error : ${e.message}", 404, ))
                }
            }
        )
        return fruits
    }

    fun deleteAllFruits() : LiveData<NetworkClass<FruitResponse>>
    {
        val response : LiveData<NetworkClass<FruitResponse>> = liveData(
            context = Dispatchers.IO,
            block = {
                try {

                    val responseContent: Response<FruitResponse> = RetrofitMain.getInstance().deleteAllFruits()
                    if (responseContent.isSuccessful) {
                        val responseBody: FruitResponse? = responseContent.body()
                        if (responseBody != null) {
                            emit(NetworkClass.Success(responseBody))
                        }
                    } else
                    {
                        emit(NetworkClass.Error("Error", 404))
                    }
                } catch (e : Exception) {

                    emit(NetworkClass.Error("Error", 404))
                }

            })
        return response

    }

    fun createNewFruit(fruit: Fruit) : LiveData<NetworkClass<FruitResponse>> {
        val response : LiveData<NetworkClass<FruitResponse>> = liveData(
            context =  Dispatchers.IO,
            block= {
            try {
                val responseContent : Response<FruitResponse> = RetrofitMain.getInstance().createNewFruitWithoutImage(fruit)
                if(responseContent.isSuccessful) {
                    val responseBody : FruitResponse? = responseContent.body()
                    if(responseBody != null){
                        emit(NetworkClass.Success(responseBody))
                    }
                }
                else {
                        emit(NetworkClass.Error("Error",  404))
                }
            }
            catch (e : Exception) {
                emit(NetworkClass.Error("Error",  404))
            }
        })
        return response
    }


}