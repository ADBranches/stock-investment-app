package com.mutebi.stockinvestmentapp.features.profile.setup

data class ProfileSetupUiState(
    val fullName: String = "",
    val phoneNumber: String = "",
    val country: String = "",
    val dateOfBirth: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
)