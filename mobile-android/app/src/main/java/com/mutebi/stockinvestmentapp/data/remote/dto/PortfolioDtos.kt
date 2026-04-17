package com.mutebi.stockinvestmentapp.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.mutebi.stockinvestmentapp.domain.model.Holding
import com.mutebi.stockinvestmentapp.domain.model.Portfolio
import com.mutebi.stockinvestmentapp.domain.model.PortfolioSummary

data class PortfolioCoreDto(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("user_id")
    val userId: Int = 0,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("cash_balance")
    val cashBalance: Double = 0.0
)

data class HoldingDto(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("portfolio_id")
    val portfolioId: Int = 0,
    @SerializedName("quantity")
    val quantity: Double = 0.0,
    @SerializedName("average_price")
    val averagePrice: Double = 0.0,
    @SerializedName("market_value")
    val marketValue: Double = 0.0,
    @SerializedName("asset")
    val asset: AssetDto? = null
) {
    fun toDomain(): Holding {
        return Holding(
            id = id,
            portfolioId = portfolioId,
            quantity = quantity,
            averagePrice = averagePrice,
            marketValue = marketValue,
            asset = asset?.toDomain()
        )
    }
}

data class PortfolioDataDto(
    @SerializedName("portfolio")
    val portfolio: PortfolioCoreDto? = null,
    @SerializedName("total_value")
    val totalValue: Double = 0.0,
    @SerializedName("holdings_count")
    val holdingsCount: Int = 0,
    @SerializedName("holdings")
    val holdings: List<HoldingDto> = emptyList(),
    @SerializedName("trades")
    val trades: List<TransactionDto> = emptyList()
) {
    fun toDomain(): Portfolio {
        return Portfolio(
            summary = PortfolioSummary(
                totalValue = totalValue,
                cashBalance = portfolio?.cashBalance ?: 0.0,
                holdingsCount = holdingsCount
            ),
            holdings = holdings.map { it.toDomain() },
            transactions = trades.map { it.toDomain() }
        )
    }
}