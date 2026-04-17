package com.mutebi.stockinvestmentapp.data.remote.api

import com.mutebi.stockinvestmentapp.data.remote.dto.ApiEnvelopeDto
import com.mutebi.stockinvestmentapp.data.remote.dto.PortfolioDataDto
import retrofit2.Response
import retrofit2.http.GET

interface PortfolioApi {
    @GET("api/v1/portfolio")
    suspend fun getPortfolio(): Response<ApiEnvelopeDto<PortfolioDataDto>>
}