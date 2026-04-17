package com.mutebi.stockinvestmentapp.features.trade.confirm

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mutebi.stockinvestmentapp.features.trade.components.OrderSummaryCard
import com.mutebi.stockinvestmentapp.features.trade.order.TradeOrderViewModel

@Composable
fun TradeConfirmScreen(
    vm: TradeOrderViewModel,
    onBack: () -> Unit,
    onTradeCompleted: () -> Unit
) {
    val state by vm.uiState.collectAsState()

    LaunchedEffect(state.submittedTransaction) {
        if (state.submittedTransaction != null) {
            onTradeCompleted()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TextButton(onClick = onBack) {
            Text("Back")
        }

        Text("Confirm trade")

        val asset = state.asset
        if (asset == null) {
            Text(state.error ?: "Asset unavailable.")
        } else {
            OrderSummaryCard(
                asset = asset,
                tradeType = state.tradeType,
                quantityText = state.quantityText
            )

            Button(
                onClick = vm::submitTrade,
                enabled = !state.isSubmitting,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (state.isSubmitting) {
                    CircularProgressIndicator()
                } else {
                    Text("Submit trade")
                }
            }
        }

        state.error?.let {
            Text(it)
        }
    }
}