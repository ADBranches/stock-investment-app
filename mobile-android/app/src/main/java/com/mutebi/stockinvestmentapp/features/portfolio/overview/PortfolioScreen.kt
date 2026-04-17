package com.mutebi.stockinvestmentapp.features.portfolio.overview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mutebi.stockinvestmentapp.features.portfolio.components.AllocationChart
import com.mutebi.stockinvestmentapp.features.portfolio.components.HoldingCard
import com.mutebi.stockinvestmentapp.features.portfolio.components.TransactionRow

@Composable
fun PortfolioScreen(
    onOpenHistory: () -> Unit,
    onTradeAsset: (Int) -> Unit,
    onOpenMarket: () -> Unit,
    vm: PortfolioViewModel = viewModel()
) {
    val state by vm.uiState.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("Portfolio")
        }

        if (state.isLoading) {
            item {
                CircularProgressIndicator()
            }
        } else {
            item {
                Text("Total value: $${"%.2f".format(state.summary.totalValue)}")
            }

            item {
                Text("Cash balance: $${"%.2f".format(state.summary.cashBalance)}")
            }

            item {
                Text("Holdings count: ${state.summary.holdingsCount}")
            }

            item {
                AllocationChart(holdings = state.holdings)
            }

            item {
                Button(onClick = onOpenHistory) {
                    Text("View transaction history")
                }
            }

            item {
                TextButton(onClick = onOpenMarket) {
                    Text("Browse market")
                }
            }

            if (state.holdings.isEmpty()) {
                item {
                    Text("No holdings yet.")
                }
            } else {
                items(state.holdings, key = { it.id }) { holding ->
                    HoldingCard(
                        holding = holding,
                        onTrade = {
                            val assetId = holding.asset?.id ?: 0
                            if (assetId > 0) onTradeAsset(assetId)
                        }
                    )
                }
            }

            if (state.recentTransactions.isNotEmpty()) {
                item {
                    Text("Recent transactions")
                }

                items(state.recentTransactions, key = { it.id }) { transaction ->
                    TransactionRow(transaction = transaction)
                }
            }
        }

        state.error?.let { errorMessage ->
            item {
                Text(errorMessage)
            }
        }
    }
}