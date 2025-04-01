package com.visma.data.receipt.repository

import com.visma.data.database.dao.ReceiptDao
import com.visma.data.database.entity.ReceiptEntity
import com.visma.domain.receipt.model.NewReceipt
import com.visma.domain.receipt.model.Receipt
import com.visma.domain.receipt.repository.ReceiptRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ReceiptRepositoryImpl @Inject constructor(
    private val receiptDao: ReceiptDao
) : ReceiptRepository {

    override fun getAllReceipts(): Flow<List<Receipt>> =
        receiptDao.getAllReceipts().mapToReceiptList()

    override suspend fun getReceipt(id: Long): Receipt =
        receiptDao.getReceipt(id).mapToReceipt()

    override suspend fun createReceipt(note: NewReceipt) =
        receiptDao.insertReceipt(note.mapToReceiptEntity())

    override suspend fun updateReceipt(note: Receipt) =
        receiptDao.updateReceipt(note.mapToReceiptEntity())

    override suspend fun deleteReceipt(note: Receipt) =
        receiptDao.deleteReceipt(note.mapToReceiptEntity())
}

private fun Flow<List<ReceiptEntity>>.mapToReceiptList() = map { list ->
    list.map { entity ->
        Receipt(
            id = entity.id,
            issuer = entity.issuer,
            date = entity.date,
            totalAmount = entity.totalAmount,
            currency = entity.currency
        )
    }
}

private fun ReceiptEntity.mapToReceipt() = Receipt(
    id = id,
    issuer = issuer,
    date = date,
    totalAmount = totalAmount,
    currency = currency
)

private fun Receipt.mapToReceiptEntity() = ReceiptEntity(
    id = id,
    issuer = issuer,
    date = date,
    totalAmount = totalAmount,
    currency = currency
)

private fun NewReceipt.mapToReceiptEntity() = ReceiptEntity(
    issuer = issuer,
    date = date,
    totalAmount = totalAmount,
    currency = currency
)