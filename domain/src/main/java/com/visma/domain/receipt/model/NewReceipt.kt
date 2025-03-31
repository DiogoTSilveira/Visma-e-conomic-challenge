package com.visma.domain.receipt.model

data class NewReceipt(
    val photoPath: String,
    val date: Long,
    val totalAmount: Double
)
