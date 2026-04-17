package com.mutebi.stockinvestmentapp.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.mutebi.stockinvestmentapp.domain.model.Asset

data class AssetListPayloadDto(
    @SerializedName("assets")
    val assets: List<AssetDto> = emptyList()
)

data class AssetDto(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("symbol")
    val symbol: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("price")
    val price: Double = 0.0,
    @SerializedName("change_percent")
    val changePercent: Double = 0.0
) {
    fun toDomain(): Asset {
        return Asset(
            id = id,
            symbol = symbol,
            name = name,
            price = price,
            changePercent = changePercent
        )
    }
}