package com.visma.data.receipt.repository

import com.visma.data.database.dao.ReceiptDao
import com.visma.data.receipt.mapper.mapToReceipt
import com.visma.data.receipt.mapper.mapToReceiptEntity
import com.visma.data.receipt.mapper.mapToReceiptList
import com.visma.domain.receipt.model.NewReceipt
import com.visma.domain.receipt.model.Receipt
import com.visma.domain.receipt.repository.ReceiptRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReceiptRepositoryImpl @Inject constructor(
    private val receiptDao: ReceiptDao
) : ReceiptRepository {

    override fun getAllReceipts(): Flow<List<Receipt>> =
        receiptDao.getAllReceipts().mapToReceiptList()

    override suspend fun getReceipt(id: Long): Receipt =
        receiptDao.getReceipt(id).mapToReceipt()

    override suspend fun createReceipt(receipt: NewReceipt) =
        receiptDao.insertReceipt(receipt.mapToReceiptEntity())

    override suspend fun deleteReceipt(id: Long) =
        receiptDao.deleteReceipt(id)
}