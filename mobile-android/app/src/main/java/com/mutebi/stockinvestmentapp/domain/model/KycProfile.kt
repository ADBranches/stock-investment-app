package com.mutebi.stockinvestmentapp.domain.model
data class KycProfile(
    val id: Int? = null,
    val userId: Int? = null,
    val firstName: String = "",
    val lastName: String = "",
    val nationalIdNumber: String = "",
    val documentType: String = "",
    val documentNumber: String = "",
    val status: String = "not_started"
)
