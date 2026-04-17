package com.mutebi.stockinvestmentapp.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.mutebi.stockinvestmentapp.domain.model.WatchlistItem

data class WatchlistListPayloadDto(
    @SerializedName("items")
    val items: List<WatchlistItemDto> = emptyList()
)

data class AddWatchlistRequestDto(
    @SerializedName("asset_id")
    val assetId: Int
)

data class WatchlistItemDto(
    @SerializedName("asset_id")
    val assetId: Int = 0,
    @SerializedName("symbol")
    val symbol: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("price")
    val price: Double = 0.0
) {
    fun toDomain(): WatchlistItem {
        return WatchlistItem(
            assetId = assetId,
            symbol = symbol,
            name = name,
            price = price
        )
    }
}