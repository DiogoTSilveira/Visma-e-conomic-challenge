package com.visma.domain.receipt.usecase

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FormatDateUseCaseTest {


    private lateinit var formatDateUseCase: FormatDateUseCase

    @Before
    fun setUp() {
        formatDateUseCase = FormatDateUseCase()
    }

    @Test
    fun `invoke should return formatted date for valid time`() {
        // Given
        val dateInMillis = Date().time
        val date = Date(dateInMillis)
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val expectedFormat = formatter.format(date)

        // When
        val result = formatDateUseCase.invoke(dateInMillis)

        // Then
        assertEquals(expectedFormat, result)
    }

    @Test
    fun `invoke should return null when dateInMillis is null`() {
        // When
        val result = formatDateUseCase.invoke(null)

        // Then
        assertEquals(null, result)
    }

}