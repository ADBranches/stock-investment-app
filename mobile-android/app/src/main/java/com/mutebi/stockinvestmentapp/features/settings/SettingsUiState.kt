package com.mutebi.stockinvestmentapp.features.settings

data class SettingsUiState(
    val privacyAccepted: Boolean = false,
    val analyticsConsent: Boolean = false,
    val marketingConsent: Boolean = false,
    val statusMessage: String? = null
)