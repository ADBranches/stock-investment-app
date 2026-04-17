package com.mutebi.stockinvestmentapp.features.trade.order

import com.mutebi.stockinvestmentapp.domain.model.Asset
import com.mutebi.stockinvestmentapp.domain.model.Transaction

data class TradeOrderUiState(
    val assetId: Int? = null,
    val asset: Asset? = null,
    val tradeType: String = "buy",
    val quantityText: String = "1",
    val isLoading: Boolean = false,
    val isSubmitting: Boolean = false,
    val error: String? = null,
    val submittedTransaction: Transaction? = null,
    val successMessage: String? = null
)