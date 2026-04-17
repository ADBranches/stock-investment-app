package com.mutebi.stockinvestmentapp.features.market.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mutebi.stockinvestmentapp.core.utils.Resource
import com.mutebi.stockinvestmentapp.data.repository.AssetRepository
import com.mutebi.stockinvestmentapp.data.repository.WatchlistRepository
import com.mutebi.stockinvestmentapp.domain.model.Asset
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class MarketListViewModel @Inject constructor(
    private val assetRepository: AssetRepository,
    private val watchlistRepository: WatchlistRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MarketListUiState(isLoading = true))
    val uiState: StateFlow<MarketListUiState> = _uiState.asStateFlow()

    private var allAssets: List<Asset> = emptyList()

    init {
        loadAssets()
    }

    fun refresh() {
        loadAssets()
    }

    fun onSearchQueryChanged(query: String) {
        val filtered = if (query.isBlank()) {
            allAssets
        } else {
            allAssets.filter {
                it.symbol.contains(query, ignoreCase = true) ||
                    it.name.contains(query, ignoreCase = true)
            }
        }

        _uiState.value = _uiState.value.copy(
            searchQuery = query,
            filteredAssets = filtered
        )
    }

    fun onSearchChange(query: String) {
        onSearchQueryChanged(query)
    }

    fun addToWatchlist(assetId: Int) {
        viewModelScope.launch {
            when (val result = watchlistRepository.addToWatchlist(assetId)) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        message = "Asset added to watchlist.",
                        error = null
                    )
                }
                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        error = result.message,
                        message = null
                    )
                }
            }
        }
    }

    private fun loadAssets() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null,
                message = null
            )

            when (val result = assetRepository.getAssets()) {
                is Resource.Success -> {
                    allAssets = result.data.orEmpty()
                    _uiState.value = MarketListUiState(
                        isLoading = false,
                        assets = allAssets,
                        filteredAssets = allAssets,
                        searchQuery = _uiState.value.searchQuery
                    )
                }
                is Resource.Error -> {
                    allAssets = emptyList()
                    _uiState.value = MarketListUiState(
                        isLoading = false,
                        assets = emptyList(),
                        filteredAssets = emptyList(),
                        searchQuery = _uiState.value.searchQuery,
                        error = result.message
                    )
                }
            }
        }
    }
}