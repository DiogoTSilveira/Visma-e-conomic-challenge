package com.visma.presentation.screen.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.visma.domain.receipt.model.Receipt
import com.visma.presentation.R
import com.visma.presentation.component.field.TextField
import com.visma.presentation.component.loader.FullScreenLoader
import com.visma.presentation.component.state.EmptyState
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
            MediumTopAppBar(
                title = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.receipts_title)
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
                .fillMaxSize()
                .padding(padding),
        ) {
            when (uiState) {
                is Loading -> {
                    FullScreenLoader()
                }

                is Success -> {
                    ReceiptListContent(
                        receipts = (uiState as Success).receipts,
                        onNavigateToAddReceipt = onNavigateToAddReceipt,
                        onSearchReceipt = viewModel::searchReceipt,
                        onFormatDate = viewModel::formatDate,
                        onFormatCurrency = viewModel::formatCurrency,
                        onNavigateToEditReceipt = onNavigateToEditReceipt,
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
    onSearchReceipt: (query: String) -> Unit,
    onFormatDate: (dateInMillis: Long) -> String?,
    onFormatCurrency: (amount: Double, currency: String) -> String,
    onNavigateToAddReceipt: () -> Unit,
    onNavigateToEditReceipt: (id: Long) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    if (receipts.isEmpty()) {
        ReceiptsEmptyState(
            isSearching = searchQuery.isNotEmpty(),
            onNavigateToAddReceipt = onNavigateToAddReceipt,
            onClearSearch = {
                searchQuery = ""
                onSearchReceipt("")
            }
        )
        return
    }

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    TextField(
        modifier = Modifier.padding(horizontal = 16.dp),
        icon = Icons.Default.Search,
        value = searchQuery,
        label = "Search",
        onValueChange = { value -> searchQuery = value },
        keyboardActions = KeyboardActions(
            onSearch = {
                focusManager.clearFocus()
                keyboardController?.hide()
                onSearchReceipt(searchQuery)
            }
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            capitalization = KeyboardCapitalization.Words,
            imeAction = ImeAction.Search
        )
    )

    LazyColumn(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(receipts) { receipt ->
            ReceiptCard(
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
private fun ReceiptsEmptyState(
    isSearching: Boolean,
    onClearSearch: () -> Unit,
    onNavigateToAddReceipt: () -> Unit
) {
    if (isSearching) {
        EmptyState(
            image = painterResource(id = R.drawable.illustration_receipt),
            title = "We found no results for your search",
            buttonText = "Clear search",
            onButtonClick = onClearSearch
        )
        return
    }

    EmptyState(
        image = painterResource(id = R.drawable.illustration_receipt),
        title = stringResource(R.string.no_receipts_registered_title),
        message = stringResource(R.string.no_receipts_registered_message),
        buttonText = stringResource(R.string.button_register_receipt),
        onButtonClick = onNavigateToAddReceipt
    )
}

@Composable
fun ReceiptCard(
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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = issuer,
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Text(
                        modifier = Modifier.weight(1f, false),
                        text = totalAmount,
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = stringResource(R.string.receipt_format, receiptId))

                    Text(text = date.orEmpty())
                }
            }
        }
    }
}
