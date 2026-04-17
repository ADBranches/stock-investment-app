package com.mutebi.stockinvestmentapp.domain.model

data class Transaction(
    val id: Int = 0,
    val assetId: Int = 0,
    val tradeType: String = "",
    val quantity: Double = 0.0,
    val price: Double = 0.0,
    val status: String = "",
    val createdAt: String = "",
    val asset: Asset? = null
)