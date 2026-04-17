package com.mutebi.stockinvestmentapp.features.market.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mutebi.stockinvestmentapp.core.utils.Resource
import com.mutebi.stockinvestmentapp.data.repository.AssetRepository
import com.mutebi.stockinvestmentapp.data.repository.WatchlistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class AssetDetailViewModel @Inject constructor(
    private val assetRepository: AssetRepository,
    private val watchlistRepository: WatchlistRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AssetDetailUiState(isLoading = false))
    val uiState: StateFlow<AssetDetailUiState> = _uiState.asStateFlow()

    fun loadAsset(assetId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null,
                message = null
            )

            when (val result = assetRepository.getAssetById(assetId)) {
                is Resource.Success -> {
                    _uiState.value = AssetDetailUiState(
                        isLoading = false,
                        asset = result.data
                    )
                }
                is Resource.Error -> {
                    _uiState.value = AssetDetailUiState(
                        isLoading = false,
                        asset = null,
                        error = result.message
                    )
                }
            }
        }
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
}