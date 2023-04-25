package com.example.productsolution.generalUtils

import com.google.gson.annotations.SerializedName

data class FruitResponse(
    @SerializedName("success")
    val success : Boolean,

    @SerializedName("message")
    val message : String,

    @SerializedName("description")
    val content : List<Fruit>

)
