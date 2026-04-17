package com.mutebi.stockinvestmentapp.features.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mutebi.stockinvestmentapp.domain.model.WatchlistItem

@Composable
fun WatchlistPreviewCard(
    items: List<WatchlistItem>,
    onOpenWatchlist: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Watchlist preview")

            if (items.isEmpty()) {
                Text("Your watchlist is empty.")
            } else {
                items.take(3).forEach { item ->
                    Text("${item.symbol} • ${item.name} • $${"%.2f".format(item.price)}")
                }
            }

            Button(onClick = onOpenWatchlist) {
                Text("View all")
            }
        }
    }
}