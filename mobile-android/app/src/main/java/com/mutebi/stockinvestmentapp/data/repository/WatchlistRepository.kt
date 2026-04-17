package com.mutebi.stockinvestmentapp.data.repository

import com.mutebi.stockinvestmentapp.core.utils.Resource
import com.mutebi.stockinvestmentapp.data.remote.api.WatchlistApi
import com.mutebi.stockinvestmentapp.data.remote.dto.AddToWatchlistRequestDto
import com.mutebi.stockinvestmentapp.data.remote.dto.WatchlistDto
import com.mutebi.stockinvestmentapp.domain.model.WatchlistItem
import javax.inject.Inject

class WatchlistRepository @Inject constructor(
    private val api: WatchlistApi
) {
    suspend fun getWatchlist(): Resource<List<WatchlistItem>> {
        return try {
            val items = api.getWatchlist()
                .data
                ?.items
                .orEmpty()
                .map { it.toDomain() }

            Resource.Success(items)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unable to load watchlist")
        }
    }

    suspend fun addToWatchlist(assetId: Int): Resource<WatchlistItem> {
        return try {
            val item = api.addToWatchlist(AddToWatchlistRequestDto(assetId))
                .data
                ?.toDomain()
                ?: return Resource.Error("Unable to add asset to watchlist")

            Resource.Success(item)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unable to add asset to watchlist")
        }
    }

    suspend fun removeFromWatchlist(assetId: Int): Resource<Unit> {
        return try {
            api.removeFromWatchlist(assetId)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unable to remove asset from watchlist")
        }
    }

    private fun WatchlistDto.toDomain(): WatchlistItem {
        return WatchlistItem(
            assetId = assetId,
            symbol = symbol,
            name = name,
            price = price
        )
    }
}