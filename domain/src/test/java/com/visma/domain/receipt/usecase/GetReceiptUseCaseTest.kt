package com.visma.domain.receipt.usecase

import com.visma.domain.receipt.model.Receipt
import com.visma.domain.receipt.repository.ReceiptRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.util.Date

@OptIn(ExperimentalCoroutinesApi::class)
class GetReceiptUseCaseTest {

    private lateinit var getReceiptUseCase: GetReceiptUseCase
    private val repository: ReceiptRepository = mock()

    @Before
    fun setUp() {
        val dispatcher = StandardTestDispatcher()
        Dispatchers.setMain(dispatcher)

        getReceiptUseCase = GetReceiptUseCase(
            repository = repository,
            dispatcher = dispatcher
        )
    }

    @Test
    fun `invoke should return receipt successfully`() = runTest {
        // Given
        val receiptId = 1L
        val expectedReceipt = Receipt(
            id = receiptId,
            issuer = "Issuer 1",
            date = Date().time,
            totalAmount = 123.0,
            currency = "EUR"
        )

        whenever(repository.getReceipt(receiptId)).thenReturn(expectedReceipt)

        // When
        val result = getReceiptUseCase.invoke(receiptId)

        // Then
        assertEquals(expectedReceipt, result)
        verify(repository).getReceipt(receiptId)
    }

    @Test
    fun `invoke should return null when receipt is not found`() = runTest {
        // Given
        val receiptId = 2L
        whenever(repository.getReceipt(receiptId)).thenReturn(null)

        // When
        val result = getReceiptUseCase.invoke(receiptId)

        // Then
        assertNull(result)
        verify(repository).getReceipt(receiptId)
    }
}
