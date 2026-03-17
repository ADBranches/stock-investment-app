package com.mutebi.stockinvestmentapp.domain.model

data class Asset(
    val id: Int,
    val symbol: String,
    val name: String,
    val price: Double,
    val changePercent: Double
)