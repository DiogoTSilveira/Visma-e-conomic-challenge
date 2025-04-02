package com.visma.domain.receipt.usecase

import java.text.NumberFormat
import java.util.Currency
import java.util.Locale
import javax.inject.Inject

class FormatCurrencyUseCase @Inject constructor() {

    operator fun invoke(amount: Double, currencyCode: String): String {
        val formatter = NumberFormat.getCurrencyInstance(Locale.getDefault()).apply {
            maximumFractionDigits = 2
            minimumFractionDigits = 2
            isGroupingUsed = true
            currency = Currency.getInstance(currencyCode)
        }
        return formatter.format(amount)
    }

}