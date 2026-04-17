package com.mutebi.stockinvestmentapp.features.trade.confirm

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mutebi.stockinvestmentapp.features.trade.order.TradeOrderViewModel

@Composable
fun TradeResultScreen(
    vm: TradeOrderViewModel,
    onDone: () -> Unit
) {
    val state by vm.uiState.collectAsState()
    val transaction = state.submittedTransaction

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Trade result")

        if (transaction != null) {
            Text(state.successMessage ?: "Trade submitted successfully.")
            Text("Type: ${transaction.tradeType}")
            Text("Quantity: ${"%.2f".format(transaction.quantity)}")
            Text("Price: $${"%.2f".format(transaction.price)}")
            Text("Status: ${transaction.status}")
        } else {
            Text(state.error ?: "Trade result unavailable.")
        }

        Button(
            onClick = onDone,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Done")
        }
    }
}