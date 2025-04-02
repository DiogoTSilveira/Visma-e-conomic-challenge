package com.visma.domain.receipt.usecase

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class FormatDateUseCase @Inject constructor() {

    companion object{
        private const val DATE_FORMAT = "dd/MM/yyyy"
    }

    operator fun invoke(dateInMillis: Long?): String? {
        return dateInMillis?.let {
            val formatter = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
            formatter.format(Date(it))
        }
    }

}