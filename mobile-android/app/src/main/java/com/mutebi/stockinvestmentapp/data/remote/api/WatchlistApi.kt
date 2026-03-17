package com.mutebi.stockinvestmentapp.data.remote.api

import com.mutebi.stockinvestmentapp.data.remote.dto.WatchlistDto
import retrofit2.http.GET

interface WatchlistApi {
    @GET("watchlist")
    suspend fun getWatchlist(): List<WatchlistDto>
}