package com.mutebi.stockinvestmentapp.features.market.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun FilterChipGroup(
    selected: String,
    onSelectedChange: (String) -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        listOf("All", "Gainers", "Decliners").forEach { label ->
            FilterChip(
                selected = selected == label,
                onClick = { onSelectedChange(label) },
                label = { Text(label) }
            )
        }
    }
}