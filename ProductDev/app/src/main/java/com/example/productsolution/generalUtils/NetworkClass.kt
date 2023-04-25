package com.example.productsolution.generalUtils

sealed class NetworkClass<out R> private constructor()
{
    class Success<out T>(val data : T) : NetworkClass<T>()
    class Error<out T>(message : String, errorCode : Int) :NetworkClass<T>()
    object Loading : NetworkClass<Nothing>()
}
