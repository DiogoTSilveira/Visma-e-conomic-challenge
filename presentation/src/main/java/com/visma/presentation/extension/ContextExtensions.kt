package com.visma.presentation.extension

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder.createSource
import android.graphics.ImageDecoder.decodeBitmap
import android.net.Uri
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val FORMAT_PREFIX = "JPEG_"
private const val FILE_DATE_PATTERN = "yyyyMMdd_HHmmss"
private const val UNDERSCORE = "_"
private const val JPG = ".jpg"

internal fun Context.uriToBitmap(uri: Uri): Bitmap? {
    return try {
        decodeBitmap(createSource(contentResolver, uri))
    } catch (exception: Exception) {
        null
    }
}

internal fun Context.loadBitmapFromInternalStorage(id: String): Bitmap? {
    val file = File(filesDir, id)
    return if (file.exists()) BitmapFactory.decodeFile(file.absolutePath) else null
}

internal fun Context.createImageFile(): File {
    val timeStamp = SimpleDateFormat(FILE_DATE_PATTERN, Locale.getDefault()).format(Date())
    val imageFileName = "$FORMAT_PREFIX$timeStamp$UNDERSCORE"
    val image = File.createTempFile(
        imageFileName,
        JPG,
        externalCacheDir
    )
    return image
}