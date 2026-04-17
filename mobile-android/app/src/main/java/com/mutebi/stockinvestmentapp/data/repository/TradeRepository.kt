package com.mutebi.stockinvestmentapp.data.repository

import com.mutebi.stockinvestmentapp.core.utils.Resource
import com.mutebi.stockinvestmentapp.data.remote.api.TradeApi
import com.mutebi.stockinvestmentapp.data.remote.dto.CreateTradeRequestDto
import com.mutebi.stockinvestmentapp.domain.model.Transaction
import javax.inject.Inject

class TradeRepository @Inject constructor(
    private val tradeApi: TradeApi
) {
    suspend fun createTrade(
        assetId: Int,
        tradeType: String,
        quantity: Double
    ): Resource<Transaction> {
        return try {
            val response = tradeApi.createTrade(
                CreateTradeRequestDto(
                    assetId = assetId,
                    tradeType = tradeType,
                    quantity = quantity
                )
            )
            val body = response.body()
            val dto = body?.data

            if (response.isSuccessful && body?.success == true && dto != null) {
                Resource.Success(dto.toDomain())
            } else {
                Resource.Error(body?.message ?: "Unable to submit trade")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unable to submit trade")
        }
    }
}