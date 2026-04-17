package com.mutebi.stockinvestmentapp.features.trade.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun TradeActionTabs(
    selected: String,
    onSelectedChange: (String) -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        listOf("buy", "sell").forEach { action ->
            FilterChip(
                selected = selected == action,
                onClick = { onSelectedChange(action) },
                label = { Text(action.uppercase()) }
            )
        }
    }
}