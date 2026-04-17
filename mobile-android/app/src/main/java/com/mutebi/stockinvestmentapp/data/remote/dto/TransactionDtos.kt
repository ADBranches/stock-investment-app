package com.mutebi.stockinvestmentapp.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.mutebi.stockinvestmentapp.domain.model.Transaction

data class TransactionListPayloadDto(
    @SerializedName("trades")
    val trades: List<TransactionDto> = emptyList()
)

data class TransactionDto(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("asset_id")
    val assetId: Int = 0,
    @SerializedName("trade_type")
    val tradeType: String = "",
    @SerializedName("quantity")
    val quantity: Double = 0.0,
    @SerializedName("price")
    val price: Double = 0.0,
    @SerializedName("status")
    val status: String = "",
    @SerializedName("created_at")
    val createdAt: String = "",
    @SerializedName("asset")
    val asset: AssetDto? = null
) {
    fun toDomain(): Transaction {
        return Transaction(
            id = id,
            assetId = assetId,
            tradeType = tradeType,
            quantity = quantity,
            price = price,
            status = status,
            createdAt = createdAt,
            asset = asset?.toDomain()
        )
    }
}