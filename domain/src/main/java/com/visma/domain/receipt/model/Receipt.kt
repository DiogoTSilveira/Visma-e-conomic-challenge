package com.visma.domain.receipt.model

data class Receipt(
    val id: Int,
    val photoPath: String,
    val date: Long,
    val totalAmount: Double
)
