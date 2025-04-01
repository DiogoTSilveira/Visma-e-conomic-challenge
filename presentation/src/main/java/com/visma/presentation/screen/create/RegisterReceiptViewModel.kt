package com.visma.presentation.screen.create

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.visma.domain.receipt.usecase.CreateReceiptUseCase
import com.visma.domain.receipt.usecase.FormatDateUseCase
import com.visma.presentation.screen.create.RegisterReceiptUiState.Idle
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
    private val createReceiptUseCase: CreateReceiptUseCase,
    private val formatDateUseCase: FormatDateUseCase
) : ViewModel() {

    private val _uiState: MutableStateFlow<RegisterReceiptUiState> = MutableStateFlow(Idle)
    val uiState = _uiState.asStateFlow()

    fun submit(data: FormData) {
        viewModelScope.launch {
            _uiState.update { RegisterReceiptUiState.Submitting }

            try {
                val receiptId = createReceiptUseCase.invoke(
                    date = data.date,
                    issuer = data.issuer,
                    amount = data.amount.toDouble(),
                    currency = data.currency
                )

                saveBitmapToInternalStorage(
                    bitmap = data.image,
                    fileName = receiptId.toString()
                )

                _uiState.update { RegisterReceiptUiState.Success }
            } catch (exception: Exception) {
                Log.e(RegisterReceiptViewModel::class.java.simpleName, exception.message.orEmpty())
                _uiState.update { RegisterReceiptUiState.Error(exception.message.orEmpty()) }
            }
        }
    }

    fun formatDate(dateInMillis: Long?) = formatDateUseCase.invoke(dateInMillis = dateInMillis)

    private fun saveBitmapToInternalStorage(bitmap: Bitmap?, fileName: String) {
        val file = File(context.filesDir, fileName)

        FileOutputStream(file).use { outputStream ->
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        }

        Log.d("Storage", "Receipt image store at: ${file.absolutePath}")
    }
}
