package com.visma.domain.receipt.repository

import com.visma.domain.receipt.model.NewReceipt
import com.visma.domain.receipt.model.Receipt
import kotlinx.coroutines.flow.Flow

interface ReceiptRepository {
    fun getAllReceipts(): Flow<List<Receipt>>
    suspend fun getReceipt(id: Long): Receipt
    suspend fun createReceipt(note: NewReceipt): Long
    suspend fun updateReceipt(note: Receipt)
    suspend fun deleteReceipt(note: Receipt)
}