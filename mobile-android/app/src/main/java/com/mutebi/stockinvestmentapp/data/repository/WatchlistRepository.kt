package com.mutebi.stockinvestmentapp.data.repository

import com.mutebi.stockinvestmentapp.core.utils.Resource
import com.mutebi.stockinvestmentapp.data.remote.api.WatchlistApi
import com.mutebi.stockinvestmentapp.data.remote.dto.AddWatchlistRequestDto
import com.mutebi.stockinvestmentapp.domain.model.WatchlistItem
import javax.inject.Inject

class WatchlistRepository @Inject constructor(
    private val api: WatchlistApi
) {
    suspend fun getWatchlist(): Resource<List<WatchlistItem>> {
        return try {
            val response = api.getWatchlist()
            val body = response.body()
            val items = body?.data?.items.orEmpty().map { it.toDomain() }

            if (response.isSuccessful && body?.success == true) {
                Resource.Success(items)
            } else {
                Resource.Error(body?.message ?: "Unable to load watchlist")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unable to load watchlist")
        }
    }

    suspend fun addToWatchlist(assetId: Int): Resource<WatchlistItem> {
        return try {
            val response = api.addToWatchlist(AddWatchlistRequestDto(assetId))
            val body = response.body()
            val dto = body?.data

            if (response.isSuccessful && body?.success == true && dto != null) {
                Resource.Success(dto.toDomain())
            } else {
                Resource.Error(body?.message ?: "Unable to add asset to watchlist")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unable to add asset to watchlist")
        }
    }

    suspend fun removeFromWatchlist(assetId: Int): Resource<String> {
        return try {
            val response = api.removeFromWatchlist(assetId)
            val body = response.body()

            if (response.isSuccessful && body?.success == true) {
                Resource.Success(body?.message ?: "Removed from watchlist")
            } else {
                Resource.Error(body?.message ?: "Unable to remove asset from watchlist")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unable to remove asset from watchlist")
        }
    }
}