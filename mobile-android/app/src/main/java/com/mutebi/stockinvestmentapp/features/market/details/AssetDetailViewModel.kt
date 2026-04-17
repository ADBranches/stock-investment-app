package com.mutebi.stockinvestmentapp.features.market.details

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

class AssetDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val assetRepository = AssetRepository(
        RetrofitProvider.assetApi(application)
    )
    private val watchlistRepository = WatchlistRepository(
        RetrofitProvider.watchlistApi(application)
    )

    private val _uiState = MutableStateFlow(AssetDetailUiState(isLoading = true))
    val uiState: StateFlow<AssetDetailUiState> = _uiState

    fun loadAsset(assetId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val assetResult = assetRepository.getAssetById(assetId)
            val watchlistResult = watchlistRepository.getWatchlist()

            val asset = when (assetResult) {
                is Resource.Success -> assetResult.data
                else -> null
            }

            val watchlistIds = when (watchlistResult) {
                is Resource.Success -> watchlistResult.data.orEmpty().map { it.assetId }.toSet()
                else -> emptySet()
            }

            val errorMessage = when {
                assetResult is Resource.Error -> assetResult.message
                watchlistResult is Resource.Error -> watchlistResult.message
                else -> null
            }

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                error = errorMessage,
                asset = asset,
                isInWatchlist = asset?.id in watchlistIds
            )
        }
    }

    fun toggleWatchlist() {
        val asset = _uiState.value.asset ?: return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(actionLoading = true, error = null)

            val result = if (_uiState.value.isInWatchlist) {
                watchlistRepository.removeFromWatchlist(asset.id)
            } else {
                watchlistRepository.addToWatchlist(asset.id)
            }

            when (result) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        actionLoading = false,
                        isInWatchlist = !_uiState.value.isInWatchlist
                    )
                }
                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        actionLoading = false,
                        error = result.message
                    )
                }
                else -> {
                    _uiState.value = _uiState.value.copy(actionLoading = false)
                }
            }
        }
    }
}