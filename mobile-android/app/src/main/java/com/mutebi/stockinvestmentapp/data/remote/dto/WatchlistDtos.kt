package com.mutebi.stockinvestmentapp.data.remote.dto

data class WatchlistDto(
    val asset_id: Int,
    val symbol: String,
    val name: String,
    val price: Double
)