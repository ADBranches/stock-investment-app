package com.mutebi.stockinvestmentapp.features.portfolio.overview

import com.mutebi.stockinvestmentapp.domain.model.Holding
import com.mutebi.stockinvestmentapp.domain.model.PortfolioSummary
import com.mutebi.stockinvestmentapp.domain.model.Transaction

data class PortfolioUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val summary: PortfolioSummary = PortfolioSummary(),
    val holdings: List<Holding> = emptyList(),
    val recentTransactions: List<Transaction> = emptyList()
)