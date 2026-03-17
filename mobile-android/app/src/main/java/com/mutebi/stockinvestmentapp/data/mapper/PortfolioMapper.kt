package com.mutebi.stockinvestmentapp.data.mapper

import com.mutebi.stockinvestmentapp.data.remote.dto.PortfolioDto
import com.mutebi.stockinvestmentapp.domain.model.Portfolio

fun PortfolioDto.toDomain(): Portfolio {
    return Portfolio(
        totalValue = total_value,
        cashBalance = cash_balance
    )
}