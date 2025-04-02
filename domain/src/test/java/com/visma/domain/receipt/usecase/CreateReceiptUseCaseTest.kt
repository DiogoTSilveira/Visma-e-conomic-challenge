package com.visma.domain.receipt.usecase

import com.visma.domain.receipt.model.NewReceipt
import com.visma.domain.receipt.repository.ReceiptRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.util.Date

class CreateReceiptUseCaseTest {

    private lateinit var createReceiptUseCase: CreateReceiptUseCase
    private val repository: ReceiptRepository = mock()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        val dispatcher = StandardTestDispatcher()
        Dispatchers.setMain(dispatcher)

        createReceiptUseCase = CreateReceiptUseCase(
            repository = repository,
            dispatcher = dispatcher
        )
    }

    @Test
    fun `invoke should return receipt ID when repository creates receipt`() = runTest {
        // Given
        val date = Date().time
        val issuer = "Issuer"
        val amount = 100.0
        val currency = "EUR"
        val expectedReceiptId = 1L

        val expectedReceipt = NewReceipt(
            issuer = issuer,
            date = date,
            totalAmount = amount,
            currency = currency
        )

        // Mock repository behavior
        whenever(repository.createReceipt(expectedReceipt)).thenReturn(1L)

        // When
        val result = createReceiptUseCase.invoke(
            date = date,
            issuer = issuer,
            amount = amount,
            currency = currency
        )

        // Then
        assertEquals(expectedReceiptId, result)
        verify(repository).createReceipt(expectedReceipt) // Verify interaction
    }

}