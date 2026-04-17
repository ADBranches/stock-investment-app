package com.mutebi.stockinvestmentapp.features.home.dashboard

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
class DashboardViewModel @Inject constructor(
    private val assetRepository: AssetRepository,
    private val watchlistRepository: WatchlistRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState(isLoading = true))
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        loadDashboard()
    }

    fun refresh() {
        loadDashboard()
    }

    private fun loadDashboard() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val assetsResult = assetRepository.getAssets()
            val watchlistResult = watchlistRepository.getWatchlist()

            val topAssets = when (assetsResult) {
                is Resource.Success -> assetsResult.data.orEmpty().take(5)
                is Resource.Error -> emptyList()
            }

            val watchlistItems = when (watchlistResult) {
                is Resource.Success -> watchlistResult.data.orEmpty().take(5)
                is Resource.Error -> emptyList()
            }

            val errorMessage = when {
                assetsResult is Resource.Error -> assetsResult.message
                watchlistResult is Resource.Error -> watchlistResult.message
                else -> null
            }

            _uiState.value = DashboardUiState(
                isLoading = false,
                topAssets = topAssets,
                watchlistItems = watchlistItems,
                error = errorMessage
            )
        }
    }
}