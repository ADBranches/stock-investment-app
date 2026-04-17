package com.mutebi.stockinvestmentapp.features.home.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mutebi.stockinvestmentapp.features.home.components.EducationPromptCard
import com.mutebi.stockinvestmentapp.features.home.components.MarketHighlightsCard
import com.mutebi.stockinvestmentapp.features.home.components.PortfolioSummaryCard
import com.mutebi.stockinvestmentapp.features.home.components.WatchlistPreviewCard

@Composable
fun DashboardScreen(
    onOpenMarket: () -> Unit,
    onOpenWatchlist: () -> Unit,
    onOpenEducation: () -> Unit,
    onOpenNotifications: () -> Unit,
    vm: DashboardViewModel = hiltViewModel()
) {
    val state by vm.uiState.collectAsState()

    if (state.isLoading) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator()
            Text("Loading dashboard...")
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Dashboard")

        PortfolioSummaryCard(
            trackedAssets = state.topAssets.size,
            watchlistCount = state.watchlistItems.size,
            onOpenWatchlist = onOpenWatchlist
        )

        MarketHighlightsCard(
            assets = state.topAssets,
            onOpenMarket = onOpenMarket
        )

        WatchlistPreviewCard(
            items = state.watchlistItems,
            onOpenWatchlist = onOpenWatchlist
        )

        EducationPromptCard(
            onOpenEducation = onOpenEducation,
            onOpenNotifications = onOpenNotifications
        )

        state.error?.let { errorMessage ->
            Text(
                text = errorMessage,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}