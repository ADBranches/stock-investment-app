package com.mutebi.stockinvestmentapp.features.home.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.item
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mutebi.stockinvestmentapp.features.home.components.EducationPromptCard
import com.mutebi.stockinvestmentapp.features.home.components.MarketHighlightsCard
import com.mutebi.stockinvestmentapp.features.home.components.PortfolioSummaryCard
import com.mutebi.stockinvestmentapp.features.home.components.WatchlistPreviewCard

@Composable
fun DashboardScreen(
    onOpenMarket: () -> Unit,
    onOpenWatchlist: () -> Unit,
    vm: DashboardViewModel = viewModel()
) {
    val state by vm.uiState.collectAsState()

    if (state.isLoading) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { CircularProgressIndicator() }
            item { Text("Loading dashboard...") }
        }
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("Dashboard")
        }

        item {
            PortfolioSummaryCard(
                trackedAssets = state.topAssets.size,
                watchlistCount = state.watchlistItems.size,
                onOpenWatchlist = onOpenWatchlist
            )
        }

        item {
            MarketHighlightsCard(
                assets = state.topAssets,
                onOpenMarket = onOpenMarket
            )
        }

        item {
            WatchlistPreviewCard(
                items = state.watchlistItems,
                onOpenWatchlist = onOpenWatchlist
            )
        }

        item {
            EducationPromptCard()
        }

        state.error?.let { errorMessage ->
            item {
                Text(
                    text = errorMessage,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}