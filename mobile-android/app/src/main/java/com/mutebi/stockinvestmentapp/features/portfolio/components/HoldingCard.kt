package com.mutebi.stockinvestmentapp.features.portfolio.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mutebi.stockinvestmentapp.features.market.components.AssetPriceRow
import com.mutebi.stockinvestmentapp.domain.model.Holding

@Composable
fun HoldingCard(
    holding: Holding,
    onTrade: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            holding.asset?.let { asset ->
                AssetPriceRow(asset = asset)
            } ?: Text("Asset unavailable")

            Text("Quantity: ${"%.2f".format(holding.quantity)}")
            Text("Average price: $${"%.2f".format(holding.averagePrice)}")
            Text("Market value: $${"%.2f".format(holding.marketValue)}")

            Button(onClick = onTrade) {
                Text("Trade")
            }
        }
    }
}