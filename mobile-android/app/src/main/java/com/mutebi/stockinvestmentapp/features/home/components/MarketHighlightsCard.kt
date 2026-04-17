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
import com.mutebi.stockinvestmentapp.domain.model.Asset

@Composable
fun MarketHighlightsCard(
    assets: List<Asset>,
    onOpenMarket: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Market highlights")

            if (assets.isEmpty()) {
                Text("No market highlights yet.")
            } else {
                assets.take(3).forEach { asset ->
                    Text("${asset.symbol} • ${asset.name} • $${"%.2f".format(asset.price)} • ${"%.2f".format(asset.changePercent)}%")
                }
            }

            Button(onClick = onOpenMarket) {
                Text("Browse market")
            }
        }
    }
}