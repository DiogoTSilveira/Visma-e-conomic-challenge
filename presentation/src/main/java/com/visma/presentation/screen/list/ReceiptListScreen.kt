package com.visma.presentation.screen.list

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.visma.domain.receipt.model.Receipt
import com.visma.presentation.R
import com.visma.presentation.component.image.AsyncImageWithLoader
import com.visma.presentation.component.state.EmptyState
import com.visma.presentation.screen.list.ReceiptListUiState.Error
import com.visma.presentation.screen.list.ReceiptListUiState.Loading
import com.visma.presentation.screen.list.ReceiptListUiState.Success
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceiptListScreen(
    viewModel: ReceiptListViewModel = hiltViewModel(),
    onNavigateToAddReceipt: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.receipts_title)) })
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
    onFormatDate: (dateInMillis: Long) -> String?,
    onFormatCurrency: (amount: Double, currency: String) -> String

) {
    if (receipts.isEmpty()) {
        EmptyState(
            image = painterResource(id = R.drawable.illustration_check_receipt),
            title = "No receipts registered",
            message = "You will see your receipts here once you start register them.",
            buttonText = "Register receipt",
            onButtonClick = onNavigateToAddReceipt
        )
        return
    }

    val context = LocalContext.current

    LazyColumn {
        items(receipts) { receipt ->
            ReceiptCard(
                receiptImage = loadBitmapFromInternalStorage(
                    context = context,
                    id = receipt.id.toString()
                ),
                date = onFormatDate(receipt.date),
                totalAmount = onFormatCurrency(receipt.totalAmount, receipt.currency),
                onReceiptSelected = {}
            )
        }
    }
}

@Composable
fun ReceiptCard(
    receiptImage: Bitmap?,
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
                val (imageRef, dateRef, amountRef) = createRefs()

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
                        .constrainAs(dateRef) {
                            top.linkTo(parent.top)
                            start.linkTo(imageRef.end)
                        },
                    text = date.orEmpty(),
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                )

                Text(
                    modifier = Modifier
                        .padding(16.dp)
                        .constrainAs(amountRef) {
                            bottom.linkTo(parent.bottom)
                            end.linkTo(parent.end)
                        },
                    text = totalAmount,
                    style = TextStyle(fontSize = 12.sp)
                )
            }
        }
    }
}

fun loadBitmapFromInternalStorage(context: Context, id: String): Bitmap? {
    val file = File(context.filesDir, id)
    return if (file.exists()) BitmapFactory.decodeFile(file.absolutePath) else null
}
