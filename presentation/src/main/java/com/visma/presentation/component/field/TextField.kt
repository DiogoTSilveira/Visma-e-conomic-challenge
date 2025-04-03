package com.visma.presentation.component.field

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun TextField(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    value: String,
    label: String,
    maxLength: Int = Int.MAX_VALUE,
    onValueChange: (String) -> Unit = {},
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    readOnly: Boolean = false,
    enabled: Boolean = true
) {
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = value,
        onValueChange = { newValue ->
            if (newValue.length <= maxLength) {
                onValueChange(newValue)
            }
        },
        label = { Text(text = label) },
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = null
            )
        },
        keyboardActions = keyboardActions,
        keyboardOptions = keyboardOptions,
        shape = RoundedCornerShape(25.dp),
        singleLine = true,
        readOnly = readOnly,
        enabled = enabled
    )

}