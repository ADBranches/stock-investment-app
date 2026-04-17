package com.mutebi.stockinvestmentapp.features.watchlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun WatchlistScreen(
    onOpenMarket: () -> Unit,
    onBack: () -> Unit,
    vm: WatchlistViewModel = hiltViewModel()
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
            items(
                items = state.items,
                key = { item -> item.assetId }
            ) { item ->
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("${item.symbol} • ${item.name}")
                        Text("Price: $${"%.2f".format(item.price)}")
                        Button(onClick = { vm.removeFromWatchlist(item.assetId) }) {
                            Text("Remove")
                        }
                    }
                }
            }
        }

        state.message?.let { message ->
            item {
                Text(message)
            }
        }

        state.error?.let { errorMessage ->
            item {
                Text(errorMessage)
            }
        }
    }
}