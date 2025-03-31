package com.visma.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "receipts")
data class ReceiptEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val photoPath: String,
    val date: Long,
    val totalAmount: Double
)

