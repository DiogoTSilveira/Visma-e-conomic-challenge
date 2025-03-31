package com.visma.presentation.screen.list

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.visma.domain.receipt.model.Receipt
import com.visma.presentation.R
import com.visma.presentation.component.image.AsyncImageWithLoader
import com.visma.presentation.component.state.EmptyState
import com.visma.presentation.screen.list.ReceiptListUiState.Error
import com.visma.presentation.screen.list.ReceiptListUiState.Loading
import com.visma.presentation.screen.list.ReceiptListUiState.Success

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
                        onNavigateToAddReceipt = onNavigateToAddReceipt
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
    onNavigateToAddReceipt: () -> Unit
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

    LazyColumn {
        items(receipts) { receipt ->
            ReceiptCard(
                receiptImage = receipt.photoPath,
                title = "Title",
                date = receipt.date.toString(),
                totalAmount = receipt.totalAmount,
                onReceiptSelected = {}
            )
        }
    }
}

@Composable
fun EmptyStateContent() {
    EmptyState(
        image = painterResource(id = R.drawable.illustration_receipt),
        title = "No receipts registered",
        message = "You will see your receipts here once you start register them.",
        buttonText = "Register receipt",
        onButtonClick = {}
    )
}

@Composable
fun ReceiptCard(
    receiptImage: String?,
    title: String,
    date: String,
    totalAmount: Double,
    onReceiptSelected: () -> Unit = {},
) {
    ConstraintLayout {
        val (card, icon) = createRefs()
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
                val (image, description) = createRefs()

                AsyncImageWithLoader(
                    modifier = Modifier.constrainAs(image) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                    },
                    image = receiptImage
                )

                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .constrainAs(description) {
                            top.linkTo(parent.top)
                            start.linkTo(image.end)
                            end.linkTo(parent.end)
                            width = Dimension.fillToConstraints
                        },
                    verticalArrangement = Arrangement.spacedBy(padding)
                ) {
                    Text(
                        text = title,
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Text(
                        text = date,
                        style = TextStyle(fontSize = 12.sp)
                    )
                }
            }
        }
    }
}
