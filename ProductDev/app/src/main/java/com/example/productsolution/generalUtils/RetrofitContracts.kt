/*
#       Written by : Rose (Pratama Azmi A)
#       Date : Unknown 
#       Text editor : AndroidStudio + VIM
*/
package com.example.productsolution.generalUtils

import retrofit2.Response
import retrofit2.http.GET

interface RetrofitContracts {


    @GET("/predictImage")
    suspend fun predictImage() : Response<ByteArray>

    @GET("/predictText")
    suspend fun predictText() : Response<ByteArray>
    
}