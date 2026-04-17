package com.mutebi.stockinvestmentapp.features.account

data class AccountUiState(
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val email: String = "",
    val fullName: String = "",
    val phoneNumber: String = "",
    val country: String = "",
    val dateOfBirth: String = "",
    val profileCompleted: Boolean = false,
    val kycStatus: String = "",
    val currentPassword: String = "",
    val newPassword: String = "",
    val confirmNewPassword: String = "",
    val successMessage: String? = null,
    val error: String? = null
)