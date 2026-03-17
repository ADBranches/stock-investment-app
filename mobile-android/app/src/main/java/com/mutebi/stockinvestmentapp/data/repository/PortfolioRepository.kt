package com.mutebi.stockinvestmentapp.data.repository

import com.mutebi.stockinvestmentapp.core.utils.Resource
import com.mutebi.stockinvestmentapp.data.mapper.toDomain
import com.mutebi.stockinvestmentapp.data.remote.api.PortfolioApi
import com.mutebi.stockinvestmentapp.domain.model.Portfolio
import javax.inject.Inject

class PortfolioRepository @Inject constructor(
    private val portfolioApi: PortfolioApi
) {
    suspend fun getPortfolio(): Resource<Portfolio> {
        return try {
            Resource.Success(portfolioApi.getPortfolio().toDomain())
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unable to load portfolio")
        }
    }
}