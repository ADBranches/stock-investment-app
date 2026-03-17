package com.mutebi.stockinvestmentapp.data.remote.dto

data class AssetDto(
    val id: Int,
    val symbol: String,
    val name: String,
    val price: Double,
    val change_percent: Double
)