package com.mutebi.stockinvestmentapp.features.home.dashboard

import com.mutebi.stockinvestmentapp.domain.model.Asset
import com.mutebi.stockinvestmentapp.domain.model.WatchlistItem

data class DashboardUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val topAssets: List<Asset> = emptyList(),
    val watchlistItems: List<WatchlistItem> = emptyList()
)