package com.visma.domain.receipt.repository

import com.visma.domain.receipt.model.NewReceipt
import com.visma.domain.receipt.model.Receipt
import kotlinx.coroutines.flow.Flow

interface ReceiptRepository {
    fun getAllNotes(): Flow<List<Receipt>>
    suspend fun createReceipt(note: NewReceipt)
    suspend fun updateReceipt(note: Receipt)
    suspend fun deleteReceipt(note: Receipt)
}