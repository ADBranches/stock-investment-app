package com.mutebi.stockinvestmentapp.features.security

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mutebi.stockinvestmentapp.core.utils.Resource
import com.mutebi.stockinvestmentapp.data.remote.network.RetrofitProvider
import com.mutebi.stockinvestmentapp.data.repository.AuthRepository
import com.mutebi.stockinvestmentapp.data.session.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SecuritySettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val sessionManager = SessionManager(application)
    private val authRepository = AuthRepository(
        authApi = RetrofitProvider.authApi(application),
        sessionManager = sessionManager
    )

    private val _uiState = MutableStateFlow(
        SecurityUiState(
            biometricEnabled = sessionManager.isBiometricLockEnabled()
        )
    )
    val uiState: StateFlow<SecurityUiState> = _uiState

    fun setBiometricAvailable(value: Boolean) {
        _uiState.value = _uiState.value.copy(biometricAvailable = value)
    }

    fun setBiometricEnabled(value: Boolean) {
        sessionManager.setBiometricLockEnabled(value)
        _uiState.value = _uiState.value.copy(
            biometricEnabled = value,
            statusMessage = if (value) {
                "Biometric lock enabled."
            } else {
                "Biometric lock disabled."
            },
            error = null
        )
    }

    fun revokeOtherSessions() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isBusy = true, error = null)

            when (val result = authRepository.revokeOtherSessions()) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isBusy = false,
                        statusMessage = result.data ?: "Other sessions revoked."
                    )
                }
                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isBusy = false,
                        error = result.message
                    )
                }
                else -> {
                    _uiState.value = _uiState.value.copy(isBusy = false)
                }
            }
        }
    }
}