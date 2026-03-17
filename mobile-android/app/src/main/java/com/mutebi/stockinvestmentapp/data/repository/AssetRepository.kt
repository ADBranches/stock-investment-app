package com.mutebi.stockinvestmentapp.data.repository

import com.mutebi.stockinvestmentapp.core.utils.Resource
import com.mutebi.stockinvestmentapp.data.mapper.toDomain
import com.mutebi.stockinvestmentapp.data.remote.api.AssetApi
import com.mutebi.stockinvestmentapp.domain.model.Asset
import javax.inject.Inject

class AssetRepository @Inject constructor(
    private val assetApi: AssetApi
) {
    suspend fun getAssets(): Resource<List<Asset>> {
        return try {
            Resource.Success(assetApi.getAssets().map { it.toDomain() })
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unable to load assets")
        }
    }
}