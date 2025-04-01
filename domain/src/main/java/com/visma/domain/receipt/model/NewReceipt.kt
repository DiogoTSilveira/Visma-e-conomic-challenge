package com.visma.domain.receipt.model

data class NewReceipt(
    val issuer: String,
    val date: Long,
    val totalAmount: Double,
    val currency: String
)
