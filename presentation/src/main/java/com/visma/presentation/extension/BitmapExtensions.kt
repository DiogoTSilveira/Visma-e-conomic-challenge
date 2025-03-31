package com.visma.presentation.extension

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream

fun Bitmap.toBase64(): String {
    val outputStream = ByteArrayOutputStream()
    compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
    val bitmap = outputStream.toByteArray()
    return Base64.encodeToString(bitmap, Base64.NO_WRAP)
}

// convert Base64 to Image bitmap
fun String.base64ToBitmap(): Bitmap? {
    return try {
        val decodedBytes = Base64.decode(this, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    } catch (exception: IllegalArgumentException) {
        null
    }
}