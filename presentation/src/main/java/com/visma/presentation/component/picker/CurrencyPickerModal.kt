package com.visma.presentation.component.picker

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.visma.presentation.extension.clickableArea
import java.util.Currency

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyPickerModal(
    selectedCurrency: String,
    onCurrencySelected: (currency: String) -> Unit,
    onDismiss: () -> Unit
) {
    val currencies = remember { Currency.getAvailableCurrencies().sortedBy { it.currencyCode } }
    var query by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            item {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = query,
                    onValueChange = {
                        println("Query: $it")
                        query = it
                    },
                    label = {
                        Text(text = "Search for currency")
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Search,
                            contentDescription = null
                        )
                    },
                    shape = RoundedCornerShape(25.dp),
                    singleLine = true
                )
            }

            items(currencies.filterQuery(query)) { currency ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = 8.dp,
                            horizontal = 16.dp
                        ).clickableArea(currency.currencyCode){
                            onCurrencySelected(currency.currencyCode)
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "${currency.displayName} (${currency.currencyCode})"
                    )

                    RadioButton(
                        selected = currency.currencyCode == selectedCurrency,
                        onClick = { onCurrencySelected(currency.currencyCode) },
                    )
                }
            }
        }
    }
}

private fun List<Currency>.filterQuery(query: String): List<Currency> {
    return filter {
        val displayName = it.displayName.lowercase()
        val currencyCode = it.currencyCode.lowercase()
        displayName.contains(query.lowercase()) || currencyCode.contains(query.lowercase())
    }
}
