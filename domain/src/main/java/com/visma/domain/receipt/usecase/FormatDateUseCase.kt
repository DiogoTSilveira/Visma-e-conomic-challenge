package com.visma.domain.receipt.usecase

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class FormatDateUseCase @Inject constructor() {

    operator fun invoke(dateInMillis: Long?): String? {
        return dateInMillis?.let {
            val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            formatter.format(Date(it))
        }
    }

}