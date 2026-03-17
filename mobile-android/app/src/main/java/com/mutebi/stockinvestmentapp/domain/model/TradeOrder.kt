package com.mutebi.stockinvestmentapp.domain.model

data class TradeOrder(
    val assetId: Int,
    val tradeType: String,
    val quantity: Int
)