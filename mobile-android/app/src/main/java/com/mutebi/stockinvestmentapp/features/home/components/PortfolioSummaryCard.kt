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

@Composable
fun PortfolioSummaryCard(
    trackedAssets: Int,
    watchlistCount: Int,
    onOpenWatchlist: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Portfolio summary")
            Text("Portfolio holdings arrive in Phase 6.")
            Text("Tracked market highlights: $trackedAssets")
            Text("Watchlist items: $watchlistCount")

            Button(onClick = onOpenWatchlist) {
                Text("Open watchlist")
            }
        }
    }
}