package com.visma.domain.receipt.usecase

import com.visma.domain.receipt.exception.DateNotValidException
import com.visma.domain.receipt.model.NewReceipt
import com.visma.domain.receipt.repository.ReceiptRepository
import javax.inject.Inject

class CreateReceiptUseCase @Inject constructor(
    private val repository: ReceiptRepository
) {
    suspend operator fun invoke(
        date: Long?,
        issuer: String,
        amount: Double,
        currency: String
    ): Long {
        if (date == null) {
            throw DateNotValidException()
        }

        return repository.createReceipt(
            NewReceipt(
                issuer = issuer,
                date = date,
                totalAmount = amount,
                currency = currency
            )
        )
    }
}