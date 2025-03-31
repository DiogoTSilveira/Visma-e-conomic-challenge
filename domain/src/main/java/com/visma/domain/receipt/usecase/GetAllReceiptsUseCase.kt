package com.visma.domain.receipt.usecase

import com.visma.domain.receipt.repository.ReceiptRepository
import javax.inject.Inject

class GetAllReceiptsUseCase @Inject constructor(
    private val repository: ReceiptRepository
) {
    operator fun invoke() = repository.getAllNotes()
}