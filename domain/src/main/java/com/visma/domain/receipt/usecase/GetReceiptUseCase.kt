package com.visma.domain.receipt.usecase

import com.visma.domain.receipt.repository.ReceiptRepository
import javax.inject.Inject

class GetReceiptUseCase @Inject constructor(
    private val repository: ReceiptRepository
) {
    suspend operator fun invoke(id: Long) = repository.getReceipt(id)
}