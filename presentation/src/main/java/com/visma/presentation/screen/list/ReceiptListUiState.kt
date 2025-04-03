package com.visma.presentation.screen.list

import com.visma.domain.receipt.model.Receipt

sealed class ReceiptListUiState {
    data object Loading : ReceiptListUiState()
    data class Success(val receipts: List<Receipt>) : ReceiptListUiState()
    data class Error(val message: String) : ReceiptListUiState()
}