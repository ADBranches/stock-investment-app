package com.mutebi.stockinvestmentapp.features.market.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mutebi.stockinvestmentapp.core.utils.Resource
import com.mutebi.stockinvestmentapp.data.remote.network.RetrofitProvider
import com.mutebi.stockinvestmentapp.data.repository.AssetRepository
import com.mutebi.stockinvestmentapp.data.repository.WatchlistRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MarketListViewModel(application: Application) : AndroidViewModel(application) {

    private val assetRepository = AssetRepository(
        RetrofitProvider.assetApi(application)
    )
    private val watchlistRepository = WatchlistRepository(
        RetrofitProvider.watchlistApi(application)
    )

    private val _uiState = MutableStateFlow(MarketListUiState(isLoading = true))
    val uiState: StateFlow<MarketListUiState> = _uiState

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val assetsResult = assetRepository.listAssets(active = true)
            val watchlistResult = watchlistRepository.getWatchlist()

            val assets = when (assetsResult) {
                is Resource.Success -> assetsResult.data.orEmpty()
                else -> emptyList()
            }

            val watchlistIds = when (watchlistResult) {
                is Resource.Success -> watchlistResult.data.orEmpty().map { it.assetId }.toSet()
                else -> emptySet()
            }

            val errorMessage = when {
                assetsResult is Resource.Error -> assetsResult.message
                watchlistResult is Resource.Error -> watchlistResult.message
                else -> null
            }

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                error = errorMessage,
                assets = assets,
                watchlistIds = watchlistIds
            )

            applyFilters()
        }
    }

    fun onSearchQueryChange(value: String) {
        _uiState.value = _uiState.value.copy(searchQuery = value)
        applyFilters()
    }

    fun onFilterChange(value: String) {
        _uiState.value = _uiState.value.copy(selectedFilter = value)
        applyFilters()
    }

    fun toggleWatchlist(assetId: Int) {
        viewModelScope.launch {
            val watchlistIds = _uiState.value.watchlistIds

            if (assetId in watchlistIds) {
                val result = watchlistRepository.removeFromWatchlist(assetId)
                if (result is Resource.Success) {
                    _uiState.value = _uiState.value.copy(
                        watchlistIds = _uiState.value.watchlistIds - assetId,
                        error = null
                    )
                } else if (result is Resource.Error) {
                    _uiState.value = _uiState.value.copy(error = result.message)
                }
            } else {
                val result = watchlistRepository.addToWatchlist(assetId)
                if (result is Resource.Success) {
                    _uiState.value = _uiState.value.copy(
                        watchlistIds = _uiState.value.watchlistIds + assetId,
                        error = null
                    )
                } else if (result is Resource.Error) {
                    _uiState.value = _uiState.value.copy(error = result.message)
                }
            }
        }
    }

    private fun applyFilters() {
        val current = _uiState.value
        val query = current.searchQuery.trim().lowercase()

        val filtered = current.assets.filter { asset ->
            val matchesQuery = query.isBlank() ||
                asset.symbol.lowercase().contains(query) ||
                asset.name.lowercase().contains(query)

            val matchesFilter = when (current.selectedFilter) {
                "Gainers" -> asset.changePercent >= 0
                "Decliners" -> asset.changePercent < 0
                else -> true
            }

            matchesQuery && matchesFilter
        }

        _uiState.value = current.copy(visibleAssets = filtered)
    }
}