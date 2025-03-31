package com.visma.presentation.screen.create

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.visma.presentation.R
import com.visma.presentation.component.camera.PhotoCamera
import com.visma.presentation.component.image.AsyncImageWithLoader
import com.visma.presentation.component.picker.DatePickerModal
import com.visma.presentation.extension.toBase64
import com.visma.presentation.extension.uriToBitmap
import com.visma.presentation.screen.create.RegisterReceiptUiState.Error
import com.visma.presentation.screen.create.RegisterReceiptUiState.Submitting
import com.visma.presentation.screen.create.RegisterReceiptUiState.Success
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
            TopAppBar(title = { Text("Register receipt") })
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

            else -> {
                RegisterReceiptContent(
                    modifier = Modifier.padding(padding),
                    onFormatDate = viewModel::formatDate
                )
            }
        }
    }
}

@Composable
private fun RegisterReceiptContent(
    modifier: Modifier,
    onFormatDate: (dateInMillis: Long) -> String?
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    var receiptPhoto by rememberSaveable { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf<String?>(null) }
    var totalAmount by rememberSaveable { mutableStateOf("") }
    var currency by rememberSaveable { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }

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
                image = receiptPhoto,
                width = screenWidth,
                height = 300.dp
            )
        }

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

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .pointerInput(selectedDate) {
                    awaitEachGesture {
                        // Modifier.clickable doesn't work for text fields, so we use Modifier.pointerInput
                        // in the Initial pass to observe events before the text field consumes them
                        // in the Main pass.
                        awaitFirstDown(pass = PointerEventPass.Initial)
                        val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                        if (upEvent != null) {
                            showDatePicker = true
                        }
                    }
                },
            value = selectedDate.orEmpty(),
            onValueChange = {},
            label = { Text(text = stringResource(R.string.date_label)) },
            placeholder = { Text(text = stringResource(R.string.select_date_label)) },
            singleLine = true,
            readOnly = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.DateRange,
                    contentDescription = null
                )
            },
            shape = RoundedCornerShape(25.dp),
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = totalAmount,
            onValueChange = { totalAmount = it },
            label = { Text(text = stringResource(R.string.total_amount_label)) },
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.AttachMoney,
                    contentDescription = null
                )
            },
            shape = RoundedCornerShape(25.dp),
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = currency,
            onValueChange = { currency = it },
            label = { Text(text = stringResource(R.string.currency_label)) },
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.AttachMoney,
                    contentDescription = null
                )
            },
            shape = RoundedCornerShape(25.dp),
        )

        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = { takePicture.value = true },
            content = {
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = stringResource(R.string.button_register_receipt)
                )
            }
        )

        Spacer(modifier = Modifier.height(32.dp))
    }

    if (showDatePicker) {
        DatePickerModal(
            onDateSelected = { date ->
                date?.let {
                    selectedDate = onFormatDate(it)
                }
            },
            onDismiss = {
                showDatePicker = false
            }
        )
    }

    PhotoCamera(
        modifier = Modifier.fillMaxSize(),
        takePicture = takePicture,
        onPhotoTaken = {
            context.uriToBitmap(it)?.let { bitmap ->
                receiptPhoto = bitmap.toBase64()
            }
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
            onFormatDate = { "31/03/2025" }
        )
    }
}