package com.mutebi.stockinvestmentapp.core.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PercentageBadge(value: Double) {
    val positive = value >= 0

    Surface(
        tonalElevation = 2.dp,
        color = if (positive) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.errorContainer
        }
    ) {
        Text(
            text = "${if (positive) "+" else ""}${"%.2f".format(value)}%",
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}