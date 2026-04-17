package com.mutebi.stockinvestmentapp.data.repository

import com.mutebi.stockinvestmentapp.core.utils.Resource
import com.mutebi.stockinvestmentapp.data.remote.api.AssetApi
import com.mutebi.stockinvestmentapp.domain.model.Asset
import javax.inject.Inject

class AssetRepository @Inject constructor(
    private val assetApi: AssetApi
) {
    suspend fun getAssets(): Resource<List<Asset>> {
        return listAssets()
    }

    suspend fun listAssets(
        search: String? = null,
        active: Boolean? = true
    ): Resource<List<Asset>> {
        return try {
            val response = assetApi.getAssets(query = search, active = active)
            val body = response.body()
            val assets = body?.data?.assets.orEmpty().map { it.toDomain() }

            if (response.isSuccessful && body?.success == true) {
                Resource.Success(assets)
            } else {
                Resource.Error(body?.message ?: "Unable to load assets")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unable to load assets")
        }
    }

    suspend fun getAssetById(assetId: Int): Resource<Asset> {
        return try {
            val response = assetApi.getAssetById(assetId)
            val body = response.body()
            val dto = body?.data

            if (response.isSuccessful && body?.success == true && dto != null) {
                Resource.Success(dto.toDomain())
            } else {
                Resource.Error(body?.message ?: "Unable to load asset details")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unable to load asset details")
        }
    }
}