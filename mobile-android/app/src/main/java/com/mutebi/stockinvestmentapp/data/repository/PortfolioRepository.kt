package com.mutebi.stockinvestmentapp.data.repository

import com.mutebi.stockinvestmentapp.core.utils.Resource
import com.mutebi.stockinvestmentapp.data.remote.api.PortfolioApi
import com.mutebi.stockinvestmentapp.domain.model.Portfolio
import javax.inject.Inject

class PortfolioRepository @Inject constructor(
    private val portfolioApi: PortfolioApi
) {
    suspend fun getPortfolio(): Resource<Portfolio> {
        return try {
            val response = portfolioApi.getPortfolio()
            val body = response.body()
            val dto = body?.data

            if (response.isSuccessful && body?.success == true && dto != null) {
                Resource.Success(dto.toDomain())
            } else {
                Resource.Error(body?.message ?: "Unable to load portfolio")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unable to load portfolio")
        }
    }
}