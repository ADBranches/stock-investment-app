package com.mutebi.stockinvestmentapp.features.market.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mutebi.stockinvestmentapp.core.ui.components.MiniLineChart
import com.mutebi.stockinvestmentapp.domain.model.Asset

@Composable
fun AssetCard(
    asset: Asset,
    isInWatchlist: Boolean,
    onClick: () -> Unit,
    onWatchlistClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AssetPriceRow(asset = asset)
            Text("Price: $${"%.2f".format(asset.price)}")

            MiniLineChart(
                values = listOf(
                    (asset.price * 0.96).toFloat(),
                    (asset.price * 1.01).toFloat(),
                    (asset.price * 0.99).toFloat(),
                    asset.price.toFloat()
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            )

            Button(onClick = onWatchlistClick) {
                Text(if (isInWatchlist) "Remove from watchlist" else "Add to watchlist")
            }
        }
    }
}