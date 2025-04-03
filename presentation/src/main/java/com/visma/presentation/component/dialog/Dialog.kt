package com.visma.presentation.component.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun Dialog(
    title: String,
    text: String,
    confirmButtonText: String,
    dismissButtonText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
){
    AlertDialog(
        title = { Text(text = title) },
        text = { Text(text = text) },
        confirmButton = {
            TextButton(
                onClick = onConfirm
            ) {
                Text(text = confirmButtonText)
            }
        },
        dismissButton = { TextButton(
            onClick = onDismiss
        ) {
            Text(text = dismissButtonText)
        } },
        onDismissRequest = onDismiss,
        shape = MaterialTheme.shapes.large
    )
}