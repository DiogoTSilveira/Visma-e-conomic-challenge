package com.visma.presentation.screen.create

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.visma.domain.receipt.usecase.CreateReceiptUseCase
import com.visma.domain.receipt.usecase.FormatDateUseCase
import com.visma.domain.receipt.usecase.GetReceiptUseCase
import com.visma.presentation.extension.loadBitmapFromInternalStorage
import com.visma.presentation.navigation.RegisterReceipt
import com.visma.presentation.screen.create.RegisterReceiptUiState.Error
import com.visma.presentation.screen.create.RegisterReceiptUiState.Idle
import com.visma.presentation.screen.create.RegisterReceiptUiState.Submitting
import com.visma.presentation.screen.create.RegisterReceiptUiState.Success
import com.visma.presentation.screen.create.model.FormData
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class RegisterReceiptViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val savedStateHandle: SavedStateHandle,
    private val getReceiptUseCase: GetReceiptUseCase,
    private val createReceiptUseCase: CreateReceiptUseCase,
    private val formatDateUseCase: FormatDateUseCase
) : ViewModel() {

    private val _uiState: MutableStateFlow<RegisterReceiptUiState> = MutableStateFlow(Idle)
    val uiState = _uiState.asStateFlow()

    private fun updateState(state: RegisterReceiptUiState) {
        _uiState.update { state }
    }

    init {
        checkReceiptForEdit()
    }

    private fun checkReceiptForEdit() {
        val arguments = savedStateHandle.toRoute<RegisterReceipt>()
        val receiptId = arguments.id

        if (receiptId == null) {
            updateState(RegisterReceiptUiState.Initialized())
            return
        }

        viewModelScope.launch {
            try {
                val receipt = getReceiptUseCase.invoke(receiptId)

                updateState(
                    RegisterReceiptUiState.Initialized(
                        data = FormData(
                            image = context.loadBitmapFromInternalStorage(id = receiptId.toString()),
                            receiptId = receiptId,
                            issuer = receipt.issuer,
                            date = receipt.date,
                            amount = receipt.totalAmount.toString(),
                            currency = receipt.currency
                        )
                    )
                )
            } catch (exception: Exception) {
                Log.e(
                    RegisterReceiptViewModel::class.java.simpleName,
                    exception.message.orEmpty()
                )
            }
        }
    }

    fun submit(data: FormData) {
        viewModelScope.launch {
            updateState(Submitting)

            try {
                val receiptId = createReceiptUseCase.invoke(
                    date = data.date,
                    issuer = data.issuer.trim(),
                    amount = data.amount.toDouble(),
                    currency = data.currency.trim()
                )

                saveBitmapToInternalStorage(
                    bitmap = data.image,
                    fileName = receiptId.toString()
                )

                updateState(Success)
            } catch (exception: Exception) {
                Log.e(RegisterReceiptViewModel::class.java.simpleName, exception.message.orEmpty())
                updateState(Error(exception.message.orEmpty()))
            }
        }
    }

    fun formatDate(dateInMillis: Long?) = formatDateUseCase.invoke(dateInMillis = dateInMillis)

    private fun saveBitmapToInternalStorage(bitmap: Bitmap?, fileName: String) {
        val file = File(context.filesDir, fileName)

        FileOutputStream(file).use { outputStream ->
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        }

        Log.d(
            RegisterReceiptViewModel::class.java.simpleName,
            "Receipt image store at: ${file.absolutePath}"
        )
    }
}
