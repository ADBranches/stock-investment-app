package com.mutebi.stockinvestmentapp.features.watchlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mutebi.stockinvestmentapp.core.utils.Resource
import com.mutebi.stockinvestmentapp.data.repository.WatchlistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class WatchlistViewModel @Inject constructor(
    private val watchlistRepository: WatchlistRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(WatchlistUiState(isLoading = true))
    val uiState: StateFlow<WatchlistUiState> = _uiState.asStateFlow()

    init {
        loadWatchlist()
    }

    fun refresh() {
        loadWatchlist()
    }

    fun loadWatchlist() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null,
                message = null
            )

            when (val result = watchlistRepository.getWatchlist()) {
                is Resource.Success -> {
                    _uiState.value = WatchlistUiState(
                        isLoading = false,
                        items = result.data.orEmpty()
                    )
                }
                is Resource.Error -> {
                    _uiState.value = WatchlistUiState(
                        isLoading = false,
                        items = emptyList(),
                        error = result.message
                    )
                }
            }
        }
    }

    fun removeFromWatchlist(assetId: Int) {
        viewModelScope.launch {
            when (val result = watchlistRepository.removeFromWatchlist(assetId)) {
                is Resource.Success -> {
                    loadWatchlist()
                }
                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.message,
                        message = null
                    )
                }
            }
        }
    }
}
