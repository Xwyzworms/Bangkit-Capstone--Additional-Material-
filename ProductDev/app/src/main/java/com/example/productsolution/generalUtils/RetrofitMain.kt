/*
#       Written by : Rose (Pratama Azmi A)
#       Date : Unknown 
#       Text editor : AndroidStudio + VIM
*/
package com.example.productsolution.generalUtils

import android.util.Log
import com.example.productsolution.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitMain private constructor() {

    companion object {

        @JvmStatic
        val BASE_URL : String ="http://10.0.2.2:3000"


        private object RetrofitHelper
        {
                val provideHttpLoggingInterceptor : HttpLoggingInterceptor = if(BuildConfig.DEBUG) HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
                else HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)

                val provideClient : OkHttpClient = OkHttpClient.Builder()
                    .addInterceptor(provideHttpLoggingInterceptor)
                    .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                    .build()

                val retrofitInstance : Retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(provideClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()


                private val CreatedInstance = retrofitInstance.create(FruitContracts::class.java)

                val API_INSTANCE : FruitContracts = CreatedInstance ?: retrofitInstance.create(FruitContracts::class.java)

        }

        fun getInstance() : FruitContracts = RetrofitHelper.API_INSTANCE

        private const val CONNECT_TIMEOUT : Long = 20
        private const val WRITE_TIMEOUT : Long = 10
        private const val READ_TIMEOUT : Long = 20
    }

}