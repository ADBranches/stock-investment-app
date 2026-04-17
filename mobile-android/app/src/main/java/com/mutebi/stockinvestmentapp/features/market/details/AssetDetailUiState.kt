package com.mutebi.stockinvestmentapp.features.market.details

import com.mutebi.stockinvestmentapp.domain.model.Asset

data class AssetDetailUiState(
    val isLoading: Boolean = false,
    val actionLoading: Boolean = false,
    val error: String? = null,
    val asset: Asset? = null,
    val isInWatchlist: Boolean = false
)