package com.visma.domain.receipt.usecase

import com.visma.domain.receipt.model.Receipt
import com.visma.domain.receipt.repository.ReceiptRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions

class DeleteReceiptUseCaseTest {

    private lateinit var deleteReceiptUseCase: DeleteReceiptUseCase
    private val repository: ReceiptRepository = mock()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        val dispatcher = StandardTestDispatcher()
        Dispatchers.setMain(dispatcher)

        deleteReceiptUseCase = DeleteReceiptUseCase(
            repository = repository,
            dispatcher = dispatcher
        )
    }

    @Test
    fun `invoke should successfully delete a receipt`() = runTest {
        // Given
        val receipt = mock<Receipt>()

        // When
        deleteReceiptUseCase.invoke(receipt)

        // Then
        verify(repository).deleteReceipt(receipt)
        verifyNoMoreInteractions(repository)
    }
}