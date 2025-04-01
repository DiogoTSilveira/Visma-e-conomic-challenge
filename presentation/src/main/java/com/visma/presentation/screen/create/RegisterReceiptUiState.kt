package com.visma.presentation.screen.create

sealed class RegisterReceiptUiState {
    data object Idle : RegisterReceiptUiState()
    data object Submitting : RegisterReceiptUiState()
    data object Success : RegisterReceiptUiState()
    data class Error(val message: String) : RegisterReceiptUiState()
}