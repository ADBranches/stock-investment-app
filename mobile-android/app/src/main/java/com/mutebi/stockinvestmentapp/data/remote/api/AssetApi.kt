package com.mutebi.stockinvestmentapp.data.remote.api

import com.mutebi.stockinvestmentapp.data.remote.dto.ApiEnvelopeDto
import com.mutebi.stockinvestmentapp.data.remote.dto.AssetDto
import com.mutebi.stockinvestmentapp.data.remote.dto.AssetListPayloadDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AssetApi {
    @GET("api/v1/assets")
    suspend fun getAssets(
        @Query("q") query: String? = null,
        @Query("active") active: Boolean? = true
    ): Response<ApiEnvelopeDto<AssetListPayloadDto>>

    @GET("api/v1/assets/{assetId}")
    suspend fun getAssetById(
        @Path("assetId") assetId: Int
    ): Response<ApiEnvelopeDto<AssetDto>>
}