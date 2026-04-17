package com.mutebi.stockinvestmentapp.features.trade.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mutebi.stockinvestmentapp.domain.model.Asset

@Composable
fun OrderSummaryCard(
    asset: Asset,
    tradeType: String,
    quantityText: String
) {
    val quantity = quantityText.toDoubleOrNull() ?: 0.0
    val estimatedValue = quantity * asset.price

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Order summary")
            Text("Asset: ${asset.symbol} • ${asset.name}")
            Text("Trade type: ${tradeType.uppercase()}")
            Text("Quantity: ${"%.2f".format(quantity)}")
            Text("Unit price: $${"%.2f".format(asset.price)}")
            Text("Estimated value: $${"%.2f".format(estimatedValue)}")
        }
    }
}