package com.mutebi.stockinvestmentapp.features.market.details

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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mutebi.stockinvestmentapp.features.market.components.AssetPriceRow

@Composable
fun AssetDetailScreen(
    assetId: Int,
    onBack: () -> Unit,
    onOpenWatchlist: () -> Unit,
    vm: AssetDetailViewModel = viewModel()
) {
    val state by vm.uiState.collectAsState()

    LaunchedEffect(assetId) {
        vm.loadAsset(assetId)
    }

    if (state.isLoading) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CircularProgressIndicator()
            Text("Loading asset details...")
        }
        return
    }

    val asset = state.asset
    if (asset == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(state.error ?: "Asset details unavailable.")
            TextButton(onClick = onBack) {
                Text("Go back")
            }
        }
        return
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

        AssetPriceRow(asset = asset)

        Text("Asset detail")
        Text("Symbol: ${asset.symbol}")
        Text("Name: ${asset.name}")
        Text("Current price: $${"%.2f".format(asset.price)}")
        Text("Change: ${"%.2f".format(asset.changePercent)}%")

        Button(
            onClick = vm::toggleWatchlist,
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.actionLoading
        ) {
            if (state.actionLoading) {
                CircularProgressIndicator()
            } else {
                Text(if (state.isInWatchlist) "Remove from watchlist" else "Add to watchlist")
            }
        }

        TextButton(onClick = onOpenWatchlist) {
            Text("Open watchlist")
        }

        state.error?.let {
            Text(it)
        }
    }
}