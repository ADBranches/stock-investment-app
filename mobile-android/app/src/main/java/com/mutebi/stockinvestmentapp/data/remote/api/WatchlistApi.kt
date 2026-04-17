package com.mutebi.stockinvestmentapp.data.remote.api

import com.mutebi.stockinvestmentapp.data.remote.dto.AddToWatchlistRequestDto
import com.mutebi.stockinvestmentapp.data.remote.dto.WatchlistBasicResponseDto
import com.mutebi.stockinvestmentapp.data.remote.dto.WatchlistItemResponseDto
import com.mutebi.stockinvestmentapp.data.remote.dto.WatchlistListResponseDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface WatchlistApi {
    @GET("watchlist")
    suspend fun getWatchlist(): WatchlistListResponseDto

    @POST("watchlist")
    suspend fun addToWatchlist(
        @Body request: AddToWatchlistRequestDto
    ): WatchlistItemResponseDto

    @DELETE("watchlist/{assetId}")
    suspend fun removeFromWatchlist(
        @Path("assetId") assetId: Int
    ): WatchlistBasicResponseDto
}