package com.mutebi.stockinvestmentapp.data.remote.api

import com.mutebi.stockinvestmentapp.data.remote.dto.AddWatchlistRequestDto
import com.mutebi.stockinvestmentapp.data.remote.dto.ApiEnvelopeDto
import com.mutebi.stockinvestmentapp.data.remote.dto.WatchlistItemDto
import com.mutebi.stockinvestmentapp.data.remote.dto.WatchlistListPayloadDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface WatchlistApi {
    @GET("api/v1/watchlist")
    suspend fun getWatchlist(): Response<ApiEnvelopeDto<WatchlistListPayloadDto>>

    @POST("api/v1/watchlist")
    suspend fun addToWatchlist(
        @Body request: AddWatchlistRequestDto
    ): Response<ApiEnvelopeDto<WatchlistItemDto>>

    @DELETE("api/v1/watchlist/{assetId}")
    suspend fun removeFromWatchlist(
        @Path("assetId") assetId: Int
    ): Response<ApiEnvelopeDto<Unit>>
}