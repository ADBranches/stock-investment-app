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
            val assets = assetApi.getAssets()
                .data
                ?.assets
                .orEmpty()
                .map { it.toDomain() }

            Resource.Success(assets)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unable to load assets")
        }
    }

    suspend fun getAssetById(assetId: Int): Resource<Asset> {
        return try {
            val asset = assetApi.getAssetById(assetId).data?.toDomain()
                ?: return Resource.Error("Asset not found")

            Resource.Success(asset)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unable to load asset")
        }
    }
}