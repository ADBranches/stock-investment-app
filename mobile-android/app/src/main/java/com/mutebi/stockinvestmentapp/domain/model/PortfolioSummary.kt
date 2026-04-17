package com.mutebi.stockinvestmentapp.domain.model

data class PortfolioSummary(
    val totalValue: Double = 0.0,
    val cashBalance: Double = 0.0,
    val holdingsCount: Int = 0
)