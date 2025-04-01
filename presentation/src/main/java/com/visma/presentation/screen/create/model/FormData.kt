package com.visma.presentation.screen.create.model

import android.graphics.Bitmap
import java.util.Date

/**
 * Represents the information filled on Register Receipt Screen
 */
data class FormData(
    val image: Bitmap? = null,
    val issuer: String = "",
    val date: Long = Date().time,
    val amount: String = "",
    val currency: String = ""
) {
    fun isValid(): Boolean {
        return image != null &&
                issuer.isNotEmpty() &&
                date >= 0 &&
                amount.isNotEmpty() &&
                currency.isNotEmpty()
    }
}
