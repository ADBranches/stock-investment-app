package com.mutebi.stockinvestmentapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AssetDto(
    val id: Int,
    val symbol: String,
    val name: String,
    val price: Double,
    @SerializedName("change_percent")
    val changePercent: Double
)

data class AssetListDataDto(
    val assets: List<AssetDto> = emptyList()
)

data class AssetListResponseDto(
    val message: String? = null,
    val data: AssetListDataDto? = null
)

data class AssetDetailResponseDto(
    val message: String? = null,
    val data: AssetDto? = null
)