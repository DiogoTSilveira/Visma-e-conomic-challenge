package com.visma.presentation.screen.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.visma.presentation.R
import com.visma.presentation.component.camera.PhotoCamera
import com.visma.presentation.component.dialog.Dialog
import com.visma.presentation.component.field.TextField
import com.visma.presentation.component.image.AsyncImageWithLoader
import com.visma.presentation.component.picker.CurrencyPickerModal
import com.visma.presentation.component.picker.DatePickerModal
import com.visma.presentation.extension.clickableArea
import com.visma.presentation.extension.uriToBitmap
import com.visma.presentation.screen.create.RegisterReceiptUiState.Error
import com.visma.presentation.screen.create.RegisterReceiptUiState.Initialized
import com.visma.presentation.screen.create.RegisterReceiptUiState.Submitting
import com.visma.presentation.screen.create.RegisterReceiptUiState.Success
import com.visma.presentation.screen.create.model.FormData
import com.visma.presentation.theme.VismaEConomicTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterReceiptScreen(
    viewModel: RegisterReceiptViewModel = hiltViewModel(),
    onClose: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.register_receipt_title),
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(
                            imageVector = Icons.Default.ChevronLeft,
                            contentDescription = null
                        )
                    }
                }
            )
        },
    ) { padding ->
        when (uiState) {
            is Submitting -> {
                CircularProgressIndicator(modifier = Modifier.fillMaxSize())
            }

            is Success -> {
                LaunchedEffect(Unit) { onClose() }
            }

            is Error -> {
                val errorMessage = (uiState as Error).message

                Text(
                    modifier = Modifier.padding(16.dp),
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error
                )
            }

            is Initialized -> {
                val data = (uiState as Initialized).data

                RegisterReceiptContent(
                    modifier = Modifier.padding(padding),
                    data = data,
                    onFormatDate = viewModel::formatDate,
                    onSubmit = viewModel::submit,
                    onDelete = viewModel::delete
                )
            }

            else -> Unit
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RegisterReceiptContent(
    modifier: Modifier,
    data: FormData,
    onFormatDate: (dateInMillis: Long?) -> String?,
    onSubmit: (data: FormData) -> Unit,
    onDelete: (id: Long) -> Unit,
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    var formData by remember(data) { mutableStateOf(data) }
    val isViewingDetails = formData.receiptId != null

    var showDatePicker by remember { mutableStateOf(false) }
    var openConfirmDeleteDialog by remember { mutableStateOf(false) }
    var showCurrencyPicker by remember { mutableStateOf(false) }
    val takePicture = remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            AsyncImageWithLoader(
                image = formData.image,
                width = screenWidth,
                height = 300.dp
            )
        }

        if (!isViewingDetails) {
            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = { takePicture.value = true },
                content = {
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = stringResource(R.string.button_take_photo)
                    )
                }
            )
        }

        TextField(
            icon = Icons.Default.Receipt,
            value = formData.issuer,
            label = stringResource(R.string.issuer_label),
            onValueChange = { value -> formData = formData.copy(issuer = value) },
            keyboardOptions = KeyboardOptions.Default.copy(
                capitalization = KeyboardCapitalization.Words
            ),
            enabled = !isViewingDetails
        )

        TextField(
            modifier = Modifier.clickableArea(
                key = formData.date,
                enabled = !isViewingDetails
            ) {
                showDatePicker = true
            },
            icon = Icons.Outlined.DateRange,
            value = onFormatDate(formData.date).orEmpty(),
            label = stringResource(R.string.date_label),
            readOnly = true,
            enabled = !isViewingDetails
        )

        TextField(
            icon = Icons.Outlined.DateRange,
            value = formData.amount,
            label = stringResource(R.string.total_amount_label),
            onValueChange = { value ->
                formData = formData.copy(amount = value)
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            enabled = !isViewingDetails
        )

        TextField(
            modifier = Modifier.clickableArea(
                key = formData.currency,
                enabled = !isViewingDetails
            ) {
                showCurrencyPicker = true
            },
            icon = Icons.Outlined.AttachMoney,
            value = formData.currency,
            label = stringResource(R.string.currency_label),
            readOnly = true,
            enabled = !isViewingDetails
        )

        if (isViewingDetails) {
            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    openConfirmDeleteDialog = true
                },
                content = {
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = stringResource(R.string.button_delete_receipt)
                    )
                }
            )
        } else {
            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    onSubmit(formData)
                },
                content = {
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = stringResource(R.string.button_register_receipt)
                    )
                },
                enabled = formData.isValid()
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }

    if (showDatePicker) {
        DatePickerModal(
            onDateSelected = { date ->
                date?.let {
                    formData = formData.copy(date = it)
                }
            },
            onDismiss = {
                showDatePicker = false
            }
        )
    }

    if (showCurrencyPicker) {
        CurrencyPickerModal(
            selectedCurrency = formData.currency,
            onCurrencySelected = { currency ->
                formData = formData.copy(currency = currency)
                showCurrencyPicker = false
            },
            onDismiss = {
                showCurrencyPicker = false
            }
        )
    }

    if (openConfirmDeleteDialog) {
        Dialog(
            title = stringResource(R.string.delete_receipt_title),
            text = stringResource(R.string.delete_receipt_message),
            confirmButtonText = stringResource(R.string.delete_button_label),
            dismissButtonText = stringResource(R.string.cancel_button_label),
            onConfirm = { formData.receiptId?.let { onDelete(it) } },
            onDismiss = { openConfirmDeleteDialog = false }
        )
    }

    PhotoCamera(
        modifier = Modifier.fillMaxSize(),
        takePicture = takePicture,
        onPhotoTaken = { uri ->
            formData = formData.copy(image = context.uriToBitmap(uri))
        },
        onCancel = { takePicture.value = false }
    )
}

@Preview(showBackground = true)
@Composable
fun RegisterReceiptContentPreview() {
    VismaEConomicTheme {
        RegisterReceiptContent(
            modifier = Modifier.padding(16.dp),
            data = FormData(),
            onFormatDate = { "31/03/2025" },
            onSubmit = {},
            onDelete = {},
        )
    }
}