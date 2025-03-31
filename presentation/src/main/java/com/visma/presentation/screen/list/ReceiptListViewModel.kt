package com.visma.presentation.screen.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.visma.domain.receipt.usecase.GetAllReceiptsUseCase
import com.visma.presentation.screen.list.ReceiptListUiState.Loading
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReceiptListViewModel @Inject constructor(
    private val getAllReceiptsUseCase: GetAllReceiptsUseCase
) : ViewModel() {

    private val _uiState: MutableStateFlow<ReceiptListUiState> = MutableStateFlow(Loading)
    val uiState = _uiState.asStateFlow()

    init {
        loadReceipts()
    }

    private fun loadReceipts() {
        viewModelScope.launch {
            getAllReceiptsUseCase.invoke().catch { throwable ->
                _uiState.update { ReceiptListUiState.Error(throwable.message.orEmpty()) }
            }.collect { list ->
                _uiState.update { ReceiptListUiState.Success(list) }
            }
        }
    }
}
