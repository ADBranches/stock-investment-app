package com.mutebi.stockinvestmentapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class WatchlistDto(
    @SerializedName("asset_id")
    val assetId: Int,
    val symbol: String,
    val name: String,
    val price: Double
)

data class WatchlistListDataDto(
    val items: List<WatchlistDto> = emptyList()
)

data class WatchlistListResponseDto(
    val message: String? = null,
    val data: WatchlistListDataDto? = null
)

data class WatchlistItemResponseDto(
    val message: String? = null,
    val data: WatchlistDto? = null
)

data class AddToWatchlistRequestDto(
    @SerializedName("asset_id")
    val assetId: Int
)

data class WatchlistBasicResponseDto(
    val message: String? = null
)