package com.mutebi.stockinvestmentapp.data.remote.api

import com.mutebi.stockinvestmentapp.data.remote.dto.PortfolioDto
import retrofit2.http.GET

interface PortfolioApi {
    @GET("portfolio")
    suspend fun getPortfolio(): PortfolioDto
}