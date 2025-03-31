package com.visma.presentation.screen.create

import com.visma.domain.receipt.model.Receipt

sealed class RegisterReceiptUiState {
    data object Idle : RegisterReceiptUiState()
    data object Submitting : RegisterReceiptUiState()
    data class Success(val data: List<Receipt>) : RegisterReceiptUiState()
    data class Error(val message: String) : RegisterReceiptUiState()
}