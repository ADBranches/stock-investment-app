package com.mutebi.stockinvestmentapp.features.security

data class SecurityUiState(
    val biometricEnabled: Boolean = false,
    val biometricAvailable: Boolean = false,
    val isBusy: Boolean = false,
    val statusMessage: String? = null,
    val error: String? = null
)