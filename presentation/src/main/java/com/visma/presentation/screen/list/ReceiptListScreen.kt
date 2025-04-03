package com.visma.presentation.screen.list

import android.graphics.Bitmap
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.visma.domain.receipt.model.Receipt
import com.visma.presentation.R
import com.visma.presentation.component.image.AsyncImageWithLoader
import com.visma.presentation.component.state.EmptyState
import com.visma.presentation.extension.loadBitmapFromInternalStorage
import com.visma.presentation.screen.list.ReceiptListUiState.Error
import com.visma.presentation.screen.list.ReceiptListUiState.Loading
import com.visma.presentation.screen.list.ReceiptListUiState.Success

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceiptListScreen(
    viewModel: ReceiptListViewModel = hiltViewModel(),
    onNavigateToAddReceipt: () -> Unit,
    onNavigateToEditReceipt: (id: Long) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.receipts_title),
                        textAlign = TextAlign.Center
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAddReceipt
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            when (uiState) {
                is Loading -> {
                    CircularProgressIndicator(modifier = Modifier.fillMaxSize())
                }

                is Success -> {
                    ReceiptListContent(
                        receipts = (uiState as Success).data,
                        onNavigateToAddReceipt = onNavigateToAddReceipt,
                        onNavigateToEditReceipt = onNavigateToEditReceipt,
                        onFormatDate = viewModel::formatDate,
                        onFormatCurrency = viewModel::formatCurrency,
                    )
                }

                is Error -> {
                    val errorMessage = (uiState as Error).message

                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
fun ReceiptListContent(
    receipts: List<Receipt>,
    onNavigateToAddReceipt: () -> Unit,
    onNavigateToEditReceipt: (id: Long) -> Unit,
    onFormatDate: (dateInMillis: Long) -> String?,
    onFormatCurrency: (amount: Double, currency: String) -> String

) {
    if (receipts.isEmpty()) {
        EmptyState(
            image = painterResource(id = R.drawable.illustration_check_receipt),
            title = stringResource(R.string.no_receipts_registered_title),
            message = stringResource(R.string.no_receipts_registered_message),
            buttonText = stringResource(R.string.button_register_receipt),
            onButtonClick = onNavigateToAddReceipt
        )
        return
    }

    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(receipts) { receipt ->
            ReceiptCard(
                receiptImage = context.loadBitmapFromInternalStorage(id = receipt.id.toString()),
                receiptId = receipt.id,
                issuer = receipt.issuer,
                date = onFormatDate(receipt.date),
                totalAmount = onFormatCurrency(receipt.totalAmount, receipt.currency),
                onReceiptSelected = { onNavigateToEditReceipt(receipt.id) }
            )
        }
    }
}

@Composable
fun ReceiptCard(
    receiptImage: Bitmap?,
    receiptId: Long,
    issuer: String,
    date: String?,
    totalAmount: String,
    onReceiptSelected: () -> Unit = {},
) {
    ConstraintLayout {
        val (card) = createRefs()
        val padding = 8.dp

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(card) { centerTo(parent) }
                .clickable(onClick = onReceiptSelected),
            elevation = CardDefaults.cardElevation(padding)
        ) {
            ConstraintLayout(
                modifier = Modifier.fillMaxWidth()
            ) {
                val (imageRef, issuerRef, dateRef, amountRef) = createRefs()

                AsyncImageWithLoader(
                    modifier = Modifier.constrainAs(imageRef) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                    },
                    image = receiptImage
                )

                Text(
                    modifier = Modifier
                        .padding(16.dp)
                        .constrainAs(issuerRef) {
                            top.linkTo(parent.top)
                            start.linkTo(imageRef.end)
                            end.linkTo(amountRef.start)
                            width = Dimension.fillToConstraints
                        },
                    text = issuer,
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                )

                Text(
                    modifier = Modifier
                        .padding(16.dp)
                        .constrainAs(dateRef) {
                            bottom.linkTo(parent.bottom)
                            end.linkTo(parent.end)
                        },
                    text = stringResource(R.string.receipt_date_format, receiptId, date.orEmpty()),
                    style = TextStyle(fontSize = 14.sp)
                )

                Text(
                    modifier = Modifier
                        .padding(16.dp)
                        .constrainAs(amountRef) {
                            top.linkTo(parent.top)
                            end.linkTo(parent.end)
                        },
                    text = totalAmount,
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}
