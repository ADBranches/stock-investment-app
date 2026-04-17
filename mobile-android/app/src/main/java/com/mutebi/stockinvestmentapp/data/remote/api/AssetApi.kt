package com.mutebi.stockinvestmentapp.data.remote.api

import com.mutebi.stockinvestmentapp.data.remote.dto.AssetDetailResponseDto
import com.mutebi.stockinvestmentapp.data.remote.dto.AssetListResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

interface AssetApi {
    @GET("assets")
    suspend fun getAssets(): AssetListResponseDto

    @GET("assets/{assetId}")
    suspend fun getAssetById(
        @Path("assetId") assetId: Int
    ): AssetDetailResponseDto
}