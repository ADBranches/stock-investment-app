package com.mutebi.stockinvestmentapp.features.market.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mutebi.stockinvestmentapp.core.ui.components.PercentageBadge
import com.mutebi.stockinvestmentapp.domain.model.Asset

@Composable
fun AssetPriceRow(asset: Asset) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("${asset.symbol} • ${asset.name}")
        PercentageBadge(value = asset.changePercent)
    }
}