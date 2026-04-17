package com.mutebi.stockinvestmentapp.data.remote.api

import com.mutebi.stockinvestmentapp.data.remote.dto.ApiEnvelopeDto
import com.mutebi.stockinvestmentapp.data.remote.dto.CreateTradeRequestDto
import com.mutebi.stockinvestmentapp.data.remote.dto.TradeResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface TradeApi {
    @POST("api/v1/trades")
    suspend fun createTrade(
        @Body request: CreateTradeRequestDto
    ): Response<ApiEnvelopeDto<TradeResponseDto>>
}