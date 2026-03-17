package com.mutebi.stockinvestmentapp.domain.model

data class UserProfile(
    val id: Int,
    val email: String,
    val fullName: String,
    val phoneNumber: String,
    val country: String,
    val dateOfBirth: String,
    val profileCompleted: Boolean,
    val kycStatus: String
)