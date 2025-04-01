package com.visma.domain.receipt.model

data class Receipt(
    val id: Long,
    val issuer: String,
    val date: Long,
    val totalAmount: Double,
    val currency: String
)
