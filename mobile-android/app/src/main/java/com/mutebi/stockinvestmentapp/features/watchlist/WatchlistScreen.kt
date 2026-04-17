package com.mutebi.stockinvestmentapp.features.watchlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun WatchlistScreen(
    onOpenMarket: () -> Unit,
    onBack: () -> Unit,
    vm: WatchlistViewModel = viewModel()
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
            Text("Watchlist")
        }

        item {
            Button(onClick = onOpenMarket) {
                Text("Browse market")
            }
        }

        if (state.isLoading) {
            item {
                CircularProgressIndicator()
            }
        } else if (state.items.isEmpty()) {
            item {
                Text("Your watchlist is empty.")
            }
        } else {
            items(state.items, key = { it.assetId }) { item ->
                androidx.compose.material3.Card {
                    androidx.compose.foundation.layout.Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("${item.symbol} • ${item.name}")
                        Text("Price: $${"%.2f".format(item.price)}")
                        Button(onClick = { vm.removeItem(item.assetId) }) {
                            Text("Remove")
                        }
                    }
                }
            }
        }

        state.error?.let { errorMessage ->
            item {
                Text(errorMessage)
            }
        }
    }
}