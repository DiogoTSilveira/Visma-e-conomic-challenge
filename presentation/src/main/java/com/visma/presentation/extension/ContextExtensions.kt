package com.visma.presentation.extension

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder.createSource
import android.graphics.ImageDecoder.decodeBitmap
import android.net.Uri

fun Context.uriToBitmap(uri: Uri): Bitmap? {
    return try {
        decodeBitmap(createSource(contentResolver, uri))
    } catch (exception: Exception) {
        null
    }
}