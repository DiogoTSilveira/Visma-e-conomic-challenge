package com.visma.domain.receipt.usecase

import com.visma.domain.receipt.di.IoDispatcher
import com.visma.domain.receipt.model.Receipt
import com.visma.domain.receipt.repository.ReceiptRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeleteReceiptUseCase @Inject constructor(
    private val repository: ReceiptRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(receipt: Receipt) = withContext(dispatcher) {
        repository.deleteReceipt(receipt)
    }
}