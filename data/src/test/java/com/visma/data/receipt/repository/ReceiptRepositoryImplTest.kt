package com.visma.data.receipt.repository

import app.cash.turbine.test
import com.visma.data.database.dao.ReceiptDao
import com.visma.data.database.entity.ReceiptEntity
import com.visma.data.receipt.mapper.mapToReceipt
import com.visma.data.receipt.mapper.mapToReceiptEntity
import com.visma.domain.receipt.model.NewReceipt
import com.visma.domain.receipt.model.Receipt
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
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.util.Date

@OptIn(ExperimentalCoroutinesApi::class)
class ReceiptRepositoryImplTest {

    private lateinit var repository: ReceiptRepositoryImpl
    private lateinit var dao: ReceiptDao

    private val expectedReceiptEntities = listOf(
        ReceiptEntity(
            id = 1,
            issuer = "Issuer 1",
            date = 123456789,
            totalAmount = 12.0,
            currency = "EUR"
        ),
        ReceiptEntity(
            id = 2,
            issuer = "Issuer 2",
            date = 123456789,
            totalAmount = 34.0,
            currency = "EUR"
        )
    )

    private val expectedReceipt = listOf(
        Receipt(
            id = 1,
            issuer = "Issuer 1",
            date = 123456789,
            totalAmount = 12.0,
            currency = "EUR"
        ),
        Receipt(
            id = 2,
            issuer = "Issuer 2",
            date = 123456789,
            totalAmount = 34.0,
            currency = "EUR"
        )
    )


    @Before
    fun setUp() {
        val dispatcher = StandardTestDispatcher()
        Dispatchers.setMain(dispatcher)

        dao = mock {
            on { getAllReceipts() } doReturn flowOf(expectedReceiptEntities)
        }

        repository = ReceiptRepositoryImpl(dao)
    }

    @Test
    fun `getAllReceipts should return flow of receipts successfully`() = runTest {
        repository.getAllReceipts().test {
            val item = awaitItem()
            assertEquals(expectedReceipt, item)
            awaitComplete()
        }
    }

    @Test
    fun `getReceipt should return receipt successfully`() = runTest {
        // Given
        val receiptId = 1L
        val expectedEntity = ReceiptEntity(
            id = receiptId,
            issuer = "Issuer 1",
            date = Date().time,
            totalAmount = 12.0,
            currency = "EUR"
        )

        whenever(dao.getReceipt(receiptId)).thenReturn(expectedEntity)

        // When
        val result = repository.getReceipt(receiptId)

        // Then
        assertEquals(expectedEntity.mapToReceipt(), result)
        verify(dao).getReceipt(receiptId)
    }

    @Test
    fun `createReceipt should call insertReceipt successfully`() = runTest {
        // Given
        val expectedNewReceipt = NewReceipt(
            issuer = "Issuer 2",
            date = Date().time,
            totalAmount = 34.0,
            currency = "EUR"
        )
        val receiptEntity = expectedNewReceipt.mapToReceiptEntity()

        // When
        repository.createReceipt(expectedNewReceipt)

        // Then
        verify(dao).insertReceipt(receiptEntity)
    }

    @Test
    fun `deleteReceipt should call deleteReceipt successfully`() = runTest {
        // Given
        val receipt = Receipt(
            id = 1,
            issuer = "Issuer 1",
            date = Date().time,
            totalAmount = 12.0,
            currency = "EUR"
        )
        val receiptEntity = receipt.mapToReceiptEntity()

        // When
        repository.deleteReceipt(receipt)

        // Then
        verify(dao).deleteReceipt(receiptEntity)
    }
}
