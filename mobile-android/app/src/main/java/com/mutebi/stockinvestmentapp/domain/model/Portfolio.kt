package com.mutebi.stockinvestmentapp.domain.model

data class Portfolio(
    val summary: PortfolioSummary = PortfolioSummary(),
    val holdings: List<Holding> = emptyList(),
    val transactions: List<Transaction> = emptyList()
)