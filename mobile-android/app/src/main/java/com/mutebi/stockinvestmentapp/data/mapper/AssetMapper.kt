package com.mutebi.stockinvestmentapp.data.mapper

import com.mutebi.stockinvestmentapp.data.remote.dto.AssetDto
import com.mutebi.stockinvestmentapp.domain.model.Asset

fun AssetDto.toDomain(): Asset {
    return Asset(
        id = id,
        symbol = symbol,
        name = name,
        price = price,
        changePercent = changePercent
    )
}