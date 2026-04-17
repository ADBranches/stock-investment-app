package com.mutebi.stockinvestmentapp.features.market.list

import com.mutebi.stockinvestmentapp.domain.model.Asset

data class MarketListUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val selectedFilter: String = "All",
    val assets: List<Asset> = emptyList(),
    val visibleAssets: List<Asset> = emptyList(),
    val watchlistIds: Set<Int> = emptySet()
)