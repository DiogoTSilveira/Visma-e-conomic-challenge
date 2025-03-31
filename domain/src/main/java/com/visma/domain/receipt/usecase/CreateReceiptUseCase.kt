package com.visma.domain.receipt.usecase

import com.visma.domain.receipt.model.NewReceipt
import com.visma.domain.receipt.repository.ReceiptRepository
import javax.inject.Inject

class CreateReceiptUseCase @Inject constructor(
    private val repository: ReceiptRepository
) {
    suspend operator fun invoke(receipt: NewReceipt) = repository.createReceipt(receipt)
}