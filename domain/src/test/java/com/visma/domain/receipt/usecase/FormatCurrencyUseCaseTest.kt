package com.visma.domain.receipt.usecase

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

class FormatCurrencyUseCaseTest {

    private lateinit var formatCurrencyUseCase: FormatCurrencyUseCase

    @Before
    fun setUp() {
        formatCurrencyUseCase = FormatCurrencyUseCase()
    }

    @Test
    fun `invoke should format EUR correctly`() {
        val result = formatCurrencyUseCase.invoke(1234.56, "EUR")

        val expected = NumberFormat.getCurrencyInstance(Locale.getDefault()).apply {
            currency = Currency.getInstance("EUR")
        }.format(1234.56)

        assertEquals(expected, result)
    }

    @Test
    fun `invoke should format USD correctly`() {
        val result = formatCurrencyUseCase.invoke(1234.56, "USD")

        val expected = NumberFormat.getCurrencyInstance(Locale.getDefault()).apply {
            currency = Currency.getInstance("USD")
        }.format(1234.56)

        assertEquals(expected, result)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `invoke should throw IllegalArgumentException for invalid currency code`() {
        formatCurrencyUseCase.invoke(100.0, "INVALID")
    }

}