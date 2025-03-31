package com.visma.presentation.screen.create

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.visma.domain.receipt.model.NewReceipt
import com.visma.domain.receipt.usecase.CreateReceiptUseCase
import com.visma.domain.receipt.usecase.FormatDateUseCase
import com.visma.presentation.screen.create.RegisterReceiptUiState.Idle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class RegisterReceiptViewModel @Inject constructor(
    private val createReceiptUseCase: CreateReceiptUseCase,
    private val formatDateUseCase: FormatDateUseCase
) : ViewModel() {

    private val _uiState: MutableStateFlow<RegisterReceiptUiState> = MutableStateFlow(Idle)
    val uiState = _uiState.asStateFlow()


    private fun register() {
        viewModelScope.launch {
            try {
                createReceiptUseCase.invoke(
                    NewReceipt(
                        photoPath = "",
                        date = Date().time,
                        totalAmount = 100.00
                    )
                )
            } catch (exception: Exception) {
                Log.e(RegisterReceiptViewModel::class.java.simpleName, exception.message.orEmpty())
            }
        }
    }

    fun formatDate(dateInMillis: Long) = formatDateUseCase.invoke(dateInMillis = dateInMillis)
}
