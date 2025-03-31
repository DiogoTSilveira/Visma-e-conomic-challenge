package com.visma.presentation.component.camera

import android.net.Uri
import androidx.camera.core.ExperimentalGetImage
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier

@androidx.annotation.OptIn(ExperimentalGetImage::class)
@Composable
fun PhotoCamera(
    modifier: Modifier = Modifier,
    takePicture: MutableState<Boolean>,
    onPhotoTaken: (photo: Uri) -> Unit,
    onCancel: () -> Unit,
) {
    if (takePicture.value) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceContainerLow)
        ) {
            CameraPreview(
                modifier = Modifier.fillMaxSize(),
                onPhotoTaken = { photo ->
                    if (photo is Uri) {
                        onPhotoTaken(photo)
                        takePicture.value = false
                    }
                },
                onCancel = {
                    onCancel()
                    takePicture.value = false
                }
            )
        }
    }
}