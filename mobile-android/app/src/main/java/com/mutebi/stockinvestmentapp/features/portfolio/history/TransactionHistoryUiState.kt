package com.mutebi.stockinvestmentapp.features.portfolio.history

import com.mutebi.stockinvestmentapp.domain.model.Transaction

data class TransactionHistoryUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val transactions: List<Transaction> = emptyList()
)