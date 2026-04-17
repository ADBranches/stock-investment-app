package com.mutebi.stockinvestmentapp.features.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.mutebi.stockinvestmentapp.data.session.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val sessionManager = SessionManager(application)

    private val _uiState = MutableStateFlow(
        SettingsUiState(
            privacyAccepted = sessionManager.isPrivacyAccepted(),
            analyticsConsent = sessionManager.isAnalyticsConsentEnabled(),
            marketingConsent = sessionManager.isMarketingConsentEnabled()
        )
    )
    val uiState: StateFlow<SettingsUiState> = _uiState

    fun onPrivacyAcceptedChange(value: Boolean) {
        sessionManager.setPrivacyAccepted(value)
        _uiState.value = _uiState.value.copy(
            privacyAccepted = value,
            statusMessage = "Privacy preference saved."
        )
    }

    fun onAnalyticsConsentChange(value: Boolean) {
        sessionManager.setAnalyticsConsentEnabled(value)
        _uiState.value = _uiState.value.copy(
            analyticsConsent = value,
            statusMessage = "Analytics consent saved."
        )
    }

    fun onMarketingConsentChange(value: Boolean) {
        sessionManager.setMarketingConsentEnabled(value)
        _uiState.value = _uiState.value.copy(
            marketingConsent = value,
            statusMessage = "Marketing consent saved."
        )
    }
}