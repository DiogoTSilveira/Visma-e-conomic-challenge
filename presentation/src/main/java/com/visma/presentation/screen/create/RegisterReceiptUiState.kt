package com.visma.presentation.screen.create

import com.visma.presentation.screen.create.model.FormData

sealed class RegisterReceiptUiState {
    data object Idle : RegisterReceiptUiState()
    data class Initialized(val data: FormData = FormData()) : RegisterReceiptUiState()
    data object Submitting : RegisterReceiptUiState()
    data object Success : RegisterReceiptUiState()
    data class Error(val message: String) : RegisterReceiptUiState()
}