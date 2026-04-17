package com.mutebi.stockinvestmentapp.features.notifications

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mutebi.stockinvestmentapp.core.utils.Resource
import com.mutebi.stockinvestmentapp.data.remote.network.RetrofitProvider
import com.mutebi.stockinvestmentapp.data.repository.NotificationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NotificationCenterViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = NotificationRepository(
        RetrofitProvider.notificationApi(application)
    )

    private val _uiState = MutableStateFlow(NotificationCenterUiState(isLoading = true))
    val uiState: StateFlow<NotificationCenterUiState> = _uiState

    init {
        loadNotifications()
    }

    fun loadNotifications() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            when (val result = repository.getNotifications()) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        notifications = result.data.orEmpty()
                    )
                }
                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
                else -> {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
            }
        }
    }

    fun markAsRead(notificationId: Int) {
        viewModelScope.launch {
            when (val result = repository.markAsRead(notificationId)) {
                is Resource.Success -> {
                    val updated = result.data
                    _uiState.value = _uiState.value.copy(
                        notifications = _uiState.value.notifications.map {
                            if (it.id == updated?.id) updated else it
                        }
                    )
                }
                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(error = result.message)
                }
                else -> Unit
            }
        }
    }
}