package com.visma.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "receipts")
data class ReceiptEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val issuer: String,
    val date: Long,
    val totalAmount: Double,
    val currency: String
)

