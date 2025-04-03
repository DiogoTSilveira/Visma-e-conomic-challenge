package com.visma.domain.receipt.usecase

import com.visma.domain.receipt.di.IoDispatcher
import com.visma.domain.receipt.repository.ReceiptRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetAllReceiptsUseCase @Inject constructor(
    private val repository: ReceiptRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {
    operator fun invoke(query: String? = null) =
        repository.getAllReceipts(query.orEmpty()).flowOn(dispatcher)
}