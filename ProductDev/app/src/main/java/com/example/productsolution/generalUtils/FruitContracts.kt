/*
#       Written by : Rose (Pratama Azmi A)
#       Date : Unknown 
#       Text editor : AndroidStudio + VIM
*/
package com.example.productsolution.generalUtils

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST

interface FruitContracts {

    //TODO
    //1. NEED To fix the API shit, add Without image ipmlementation


    @GET("/fruit")
    suspend fun getFruits() : Response<FruitResponse>

    @POST("fruit")
    suspend fun createNewFruitWithImage(@Body Fruit: Fruit) : Response<FruitResponse>

    @DELETE("fruit")
    suspend fun deleteAllFruits() : Response<FruitResponse>

    @GET("/fruit/{name}")
    suspend fun getSpecificFruit(name : String) : Response<FruitResponse>

    @DELETE("/fruit/{name}")
    suspend fun deleteSpecificFruit(name : String) : Response<FruitResponse>

    @POST("fruit")
    suspend fun createNewFruitWithoutImage(@Body Fruit: Fruit) : Response<FruitResponse>



}