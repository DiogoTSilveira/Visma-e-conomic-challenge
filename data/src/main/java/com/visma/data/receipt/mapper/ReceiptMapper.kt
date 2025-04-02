package com.visma.data.receipt.mapper

import com.visma.data.database.entity.ReceiptEntity
import com.visma.domain.receipt.model.NewReceipt
import com.visma.domain.receipt.model.Receipt
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal fun Flow<List<ReceiptEntity>>.mapToReceiptList() = map { list ->
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

internal fun ReceiptEntity.mapToReceipt() = Receipt(
    id = id,
    issuer = issuer,
    date = date,
    totalAmount = totalAmount,
    currency = currency
)

internal fun Receipt.mapToReceiptEntity() = ReceiptEntity(
    id = id,
    issuer = issuer,
    date = date,
    totalAmount = totalAmount,
    currency = currency
)

internal fun NewReceipt.mapToReceiptEntity() = ReceiptEntity(
    issuer = issuer,
    date = date,
    totalAmount = totalAmount,
    currency = currency
)