package com.visma.domain.receipt.usecase

import app.cash.turbine.test
import com.visma.domain.receipt.model.Receipt
import com.visma.domain.receipt.repository.ReceiptRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import java.util.Date

@OptIn(ExperimentalCoroutinesApi::class)
class GetAllReceiptsUseCaseTest {

    private lateinit var getAllReceiptsUseCase: GetAllReceiptsUseCase

    companion object {
        private const val EMPTY_TEXT = ""
    }

    private val expectedReceipts = listOf(
        Receipt(
            id = 1,
            issuer = "Issuer 1",
            date = Date().time,
            totalAmount = 12.0,
            currency = "EUR"
        ),
        Receipt(
            id = 2,
            issuer = "Issuer 2",
            date = Date().time,
            totalAmount = 34.0,
            currency = "EUR"
        )
    )

    @Before
    fun setUp() {
        val dispatcher = StandardTestDispatcher()
        Dispatchers.setMain(dispatcher)

        val repository: ReceiptRepository = mock {
            on { getAllReceipts(EMPTY_TEXT) } doReturn flowOf(expectedReceipts)
        }

        getAllReceiptsUseCase = GetAllReceiptsUseCase(
            repository = repository,
            dispatcher = dispatcher
        )
    }

    @Test
    fun `invoke should return flow of receipt list`() = runTest {
        getAllReceiptsUseCase.invoke().test {
            // When
            val result = awaitItem()

            // Then
            assertEquals(expectedReceipts, result)
            awaitComplete()
        }
    }
}
