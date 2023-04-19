/*
package com.example.productsolution.imageStudyCases
#       Written by : Rose (Pratama Azmi A)
#       Date : Unknown 
#       Text editor : AndroidStudio + VIM
*/
package com.example.productsolution.imageStudyCases

import android.graphics.Bitmap
import kotlin.math.max

fun scaleBitmapDown(bitmap : Bitmap, maxDimension : Int) : Bitmap
{
    // Basically downgrade the size of image so you can send it through the network or even passing it to another activity
    //TODO
    // Scale down the original size ( To the max Dimension ) but still retain the ratio
    // Return bitmap

    // Example 640 * 320, max dim = 480
    // Height > width
    // Width = 480 * 320 /  640
    // Width = 240
    val originalWidth : Int = bitmap.width
    val originalHeight : Int = bitmap.height

    var desiredWidth : Int = maxDimension
    var desiredHeight : Int = maxDimension

    if(originalHeight > originalWidth)
    {
        // Trying to make the scaler same as before
        // 480 * 320
        desiredHeight = maxDimension
        val scaler = (originalWidth.toFloat() / originalHeight.toFloat()).toInt()
        desiredWidth = (desiredHeight * scaler) // Rumus untuk retain but memperkecil
    }
    else if(originalHeight < originalWidth)
    {
        desiredWidth = maxDimension
        val scaler = (originalWidth.toFloat() / originalHeight.toFloat()).toInt()
        desiredHeight = (desiredWidth * scaler)
    }
    else
    {
        desiredHeight = maxDimension
        desiredWidth = maxDimension
    }

    return Bitmap.createScaledBitmap(bitmap, desiredWidth, desiredHeight, false)
}
