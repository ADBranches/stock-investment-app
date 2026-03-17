package com.mutebi.stockinvestmentapp.domain.model

data class WatchlistItem(
    val assetId: Int,
    val symbol: String,
    val name: String,
    val price: Double
)