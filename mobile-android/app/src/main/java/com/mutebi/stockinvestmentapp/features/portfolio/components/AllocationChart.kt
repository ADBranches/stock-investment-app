package com.mutebi.stockinvestmentapp.features.portfolio.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mutebi.stockinvestmentapp.domain.model.Holding

@Composable
fun AllocationChart(
    holdings: List<Holding>
) {
    val total = holdings.sumOf { it.marketValue }.takeIf { it > 0.0 } ?: 1.0

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Allocation")

            if (holdings.isEmpty()) {
                Text("No allocation to display yet.")
            } else {
                holdings.forEach { holding ->
                    val label = holding.asset?.symbol ?: "Unknown"
                    val fraction = (holding.marketValue / total).toFloat().coerceIn(0f, 1f)

                    Text("$label • $${"%.2f".format(holding.marketValue)}")
                    LinearProgressIndicator(
                        progress = { fraction },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                    )
                }
            }
        }
    }
}