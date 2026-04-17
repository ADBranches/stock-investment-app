package com.mutebi.stockinvestmentapp.features.market.details

import com.mutebi.stockinvestmentapp.domain.model.Asset

data class AssetDetailUiState(
    val isLoading: Boolean = false,
    val asset: Asset? = null,
    val error: String? = null,
    val message: String? = null
)