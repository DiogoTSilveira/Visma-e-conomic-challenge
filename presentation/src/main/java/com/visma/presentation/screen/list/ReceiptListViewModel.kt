package com.visma.presentation.screen.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.visma.domain.receipt.usecase.FormatCurrencyUseCase
import com.visma.domain.receipt.usecase.FormatDateUseCase
import com.visma.domain.receipt.usecase.GetAllReceiptsUseCase
import com.visma.presentation.screen.list.ReceiptListUiState.Error
import com.visma.presentation.screen.list.ReceiptListUiState.Loading
import com.visma.presentation.screen.list.ReceiptListUiState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReceiptListViewModel @Inject constructor(
    private val getAllReceiptsUseCase: GetAllReceiptsUseCase,
    private val formatDateUseCase: FormatDateUseCase,
    private val formatCurrencyUseCase: FormatCurrencyUseCase,
) : ViewModel() {

    private val _uiState: MutableStateFlow<ReceiptListUiState> = MutableStateFlow(Loading)
    val uiState = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")

    private fun updateState(state: ReceiptListUiState) {
        _uiState.update { state }
    }

    init {
        getReceipts()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getReceipts() {
        viewModelScope.launch {
            _searchQuery
                .flatMapLatest { query ->
                    getAllReceiptsUseCase.invoke(query)
                }.catch {
                    updateState(Error)
                }.collect { list ->
                    updateState(Success(receipts = list))
                }
        }
    }

    fun retry() {
        viewModelScope.launch {
            getAllReceiptsUseCase.invoke()
                .onStart {
                    updateState(Loading)
                }.catch {
                    updateState(Error)
                }.firstOrNull()
        }
    }

    fun searchReceipt(query: String) {
        _searchQuery.update { query }
    }

    fun formatDate(dateInMillis: Long?) = formatDateUseCase.invoke(dateInMillis = dateInMillis)

    fun formatCurrency(amount: Double, currency: String) = formatCurrencyUseCase.invoke(
        amount = amount,
        currencyCode = currency
    )
}
