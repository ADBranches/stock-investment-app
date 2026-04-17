package com.mutebi.stockinvestmentapp.features.market.list

import com.mutebi.stockinvestmentapp.domain.model.Asset

data class MarketListUiState(
    val isLoading: Boolean = false,
    val assets: List<Asset> = emptyList(),
    val filteredAssets: List<Asset> = emptyList(),
    val searchQuery: String = "",
    val error: String? = null,
    val message: String? = null
)