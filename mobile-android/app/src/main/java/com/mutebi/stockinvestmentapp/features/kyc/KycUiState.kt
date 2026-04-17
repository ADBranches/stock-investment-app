package com.mutebi.stockinvestmentapp.features.kyc

data class KycUiState(
    val firstName: String = "",
    val lastName: String = "",
    val nationalIdNumber: String = "",
    val documentType: String = "",
    val documentNumber: String = "",
    val status: String = "not_started",
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
)