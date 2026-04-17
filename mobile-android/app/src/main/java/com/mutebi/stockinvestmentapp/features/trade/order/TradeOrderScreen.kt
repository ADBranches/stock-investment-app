package com.mutebi.stockinvestmentapp.features.trade.order

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
import com.mutebi.stockinvestmentapp.features.trade.components.QuantitySelector
import com.mutebi.stockinvestmentapp.features.trade.components.TradeActionTabs

@Composable
fun TradeOrderScreen(
    assetId: Int,
    vm: TradeOrderViewModel,
    onBack: () -> Unit,
    onContinue: () -> Unit
) {
    val state by vm.uiState.collectAsState()

    LaunchedEffect(assetId) {
        vm.clearResult()
        vm.loadAsset(assetId)
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

        Text("Trade order")

        if (state.isLoading) {
            CircularProgressIndicator()
        } else {
            val asset = state.asset
            if (asset == null) {
                Text(state.error ?: "Asset unavailable.")
            } else {
                Text("${asset.symbol} • ${asset.name}")
                Text("Current price: $${"%.2f".format(asset.price)}")

                TradeActionTabs(
                    selected = state.tradeType,
                    onSelectedChange = vm::onTradeTypeChange
                )

                QuantitySelector(
                    value = state.quantityText,
                    onValueChange = vm::onQuantityChange
                )

                OrderSummaryCard(
                    asset = asset,
                    tradeType = state.tradeType,
                    quantityText = state.quantityText
                )

                Button(
                    onClick = {
                        if (vm.canProceedToConfirm()) {
                            onContinue()
                        } else {
                            vm.onQuantityChange(state.quantityText)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Continue")
                }
            }
        }

        state.error?.let {
            Text(it)
        }
    }
}