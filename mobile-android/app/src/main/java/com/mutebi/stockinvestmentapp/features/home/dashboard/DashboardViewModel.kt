package com.mutebi.stockinvestmentapp.features.home.dashboard

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

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val assetRepository = AssetRepository(
        RetrofitProvider.assetApi(application)
    )

    private val watchlistRepository = WatchlistRepository(
        RetrofitProvider.watchlistApi(application)
    )

    private val _uiState = MutableStateFlow(DashboardUiState(isLoading = true))
    val uiState: StateFlow<DashboardUiState> = _uiState

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

            val watchlistItems = when (watchlistResult) {
                is Resource.Success -> watchlistResult.data.orEmpty()
                else -> emptyList()
            }

            val errorMessage = when {
                assetsResult is Resource.Error -> assetsResult.message
                watchlistResult is Resource.Error -> watchlistResult.message
                else -> null
            }

            _uiState.value = DashboardUiState(
                isLoading = false,
                error = errorMessage,
                topAssets = assets.sortedByDescending { it.changePercent }.take(5),
                watchlistItems = watchlistItems.take(4)
            )
        }
    }
}