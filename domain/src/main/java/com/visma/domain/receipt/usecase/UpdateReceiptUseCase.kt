package com.visma.domain.receipt.usecase

import com.visma.domain.receipt.model.Receipt
import com.visma.domain.receipt.repository.ReceiptRepository
import javax.inject.Inject

class UpdateReceiptUseCase @Inject constructor(
    private val repository: ReceiptRepository
) {
    suspend operator fun invoke(receipt: Receipt) = repository.updateReceipt(receipt)
}