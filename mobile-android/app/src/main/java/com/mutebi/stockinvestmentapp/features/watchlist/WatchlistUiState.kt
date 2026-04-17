package com.mutebi.stockinvestmentapp.features.watchlist

import com.mutebi.stockinvestmentapp.domain.model.WatchlistItem

data class WatchlistUiState(
    val isLoading: Boolean = false,
    val items: List<WatchlistItem> = emptyList(),
    val error: String? = null,
    val message: String? = null
)