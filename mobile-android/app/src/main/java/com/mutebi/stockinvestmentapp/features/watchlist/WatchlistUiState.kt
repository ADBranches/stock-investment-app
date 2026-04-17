package com.mutebi.stockinvestmentapp.features.watchlist

import com.mutebi.stockinvestmentapp.domain.model.WatchlistItem

data class WatchlistUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val items: List<WatchlistItem> = emptyList()
)