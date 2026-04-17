package com.mutebi.stockinvestmentapp.features.market.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mutebi.stockinvestmentapp.features.market.components.AssetCard
import com.mutebi.stockinvestmentapp.features.market.components.MarketSearchBar

@Composable
fun MarketListScreen(
    onAssetClick: (Int) -> Unit,
    onOpenWatchlist: () -> Unit,
    vm: MarketListViewModel = hiltViewModel()
) {
    val state by vm.uiState.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("Market")
        }

        item {
            MarketSearchBar(
                value = state.searchQuery,
                onValueChange = vm::onSearchChange
            )
        }

        item {
            TextButton(onClick = onOpenWatchlist) {
                Text("Open watchlist")
            }
        }

        if (state.isLoading) {
            item {
                CircularProgressIndicator()
            }
        } else if (state.filteredAssets.isEmpty()) {
            item {
                Text("No assets available.")
            }
        } else {
            items(
                items = state.filteredAssets,
                key = { asset -> asset.id }
            ) { asset ->
                AssetCard(
                    asset = asset,
                    isInWatchlist = false,
                    onClick = { onAssetClick(asset.id) },
                    onWatchlistClick = { vm.addToWatchlist(asset.id) }
                )
            }
        }

        state.message?.let { message ->
            item {
                Text(message)
            }
        }

        state.error?.let { errorMessage ->
            item {
                Text(errorMessage)
            }
        }
    }
}