package com.visma.presentation.extension

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder.createSource
import android.graphics.ImageDecoder.decodeBitmap
import android.net.Uri
import java.io.File

fun Context.uriToBitmap(uri: Uri): Bitmap? {
    return try {
        decodeBitmap(createSource(contentResolver, uri))
    } catch (exception: Exception) {
        null
    }
}

fun Context.loadBitmapFromInternalStorage(id: String): Bitmap? {
    val file = File(filesDir, id)
    return if (file.exists()) BitmapFactory.decodeFile(file.absolutePath) else null
}