package com.mutebi.stockinvestmentapp.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.mutebi.stockinvestmentapp.domain.model.UserProfile

data class ApiEnvelopeDto<T>(
    @SerializedName("success")
    val success: Boolean = false,
    @SerializedName("message")
    val message: String = "",
    @SerializedName("data")
    val data: T? = null,
    @SerializedName("token")
    val token: String? = null
)

data class LoginRequestDto(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String
)

data class RegisterRequestDto(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("full_name")
    val fullName: String
)
data class ForgotPasswordRequestDto(
    @SerializedName("email")
    val email: String
)

data class AuthUserDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("email")
    val email: String,
    @SerializedName("full_name")
    val fullName: String? = null,
    @SerializedName("profile_completed")
    val profileCompleted: Boolean = false,
    @SerializedName("kyc_status")
    val kycStatus: String? = null
) {
    fun toDomain(): UserProfile {
        return UserProfile(
            id = id,
            email = email,
            fullName = fullName.orEmpty(),
            phoneNumber = "",
            country = "",
            dateOfBirth = "",
            profileCompleted = profileCompleted,
            kycStatus = kycStatus.orEmpty()
        )
    }
}

data class AuthPayloadDto(
    @SerializedName("token")
    val token: String? = null,
    @SerializedName("access_token")
    val accessToken: String? = null,
    @SerializedName("user")
    val user: AuthUserDto? = null
)

/**
 * Keep this temporarily if some API calls still expect the old flat response.
 */
data class AuthResponseDto(
    @SerializedName("success")
    val success: Boolean = false,
    @SerializedName("message")
    val message: String = "",
    @SerializedName("token")
    val token: String? = null,
    @SerializedName("user")
    val user: AuthUserDto? = null
)

data class GenericMessageResponseDto(
    @SerializedName("success")
    val success: Boolean = false,
    @SerializedName("message")
    val message: String = ""
)