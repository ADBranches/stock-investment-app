package com.mutebi.stockinvestmentapp.features.portfolio.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mutebi.stockinvestmentapp.features.portfolio.components.TransactionRow

@Composable
fun TransactionHistoryScreen(
    onBack: () -> Unit,
    vm: TransactionHistoryViewModel = viewModel()
) {
    val state by vm.uiState.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            TextButton(onClick = onBack) {
                Text("Back")
            }
        }

        item {
            Text("Transaction history")
        }

        if (state.isLoading) {
            item {
                CircularProgressIndicator()
            }
        } else if (state.transactions.isEmpty()) {
            item {
                Text("No transactions yet.")
            }
        } else {
            items(state.transactions, key = { it.id }) { transaction ->
                TransactionRow(transaction = transaction)
            }
        }

        state.error?.let { errorMessage ->
            item {
                Text(errorMessage)
            }
        }
    }
}