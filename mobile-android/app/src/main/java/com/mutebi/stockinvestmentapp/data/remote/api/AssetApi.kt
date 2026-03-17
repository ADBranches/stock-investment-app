package com.mutebi.stockinvestmentapp.data.remote.api

import com.mutebi.stockinvestmentapp.data.remote.dto.AssetDto
import retrofit2.http.GET

interface AssetApi {
    @GET("assets")
    suspend fun getAssets(): List<AssetDto>
}