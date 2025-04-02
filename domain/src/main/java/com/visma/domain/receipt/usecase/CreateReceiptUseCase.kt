package com.visma.domain.receipt.usecase

import com.visma.domain.receipt.di.IoDispatcher
import com.visma.domain.receipt.exception.DateNotValidException
import com.visma.domain.receipt.model.NewReceipt
import com.visma.domain.receipt.repository.ReceiptRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CreateReceiptUseCase @Inject constructor(
    private val repository: ReceiptRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(
        date: Long?,
        issuer: String,
        amount: Double,
        currency: String
    ): Long = withContext(dispatcher) {
        if (date == null) {
            throw DateNotValidException()
        }

        repository.createReceipt(
            NewReceipt(
                issuer = issuer,
                date = date,
                totalAmount = amount,
                currency = currency
            )
        )
    }
}