package com.mutebi.stockinvestmentapp.domain.model

data class Holding(
    val id: Int = 0,
    val portfolioId: Int = 0,
    val quantity: Double = 0.0,
    val averagePrice: Double = 0.0,
    val marketValue: Double = 0.0,
    val asset: Asset? = null
)