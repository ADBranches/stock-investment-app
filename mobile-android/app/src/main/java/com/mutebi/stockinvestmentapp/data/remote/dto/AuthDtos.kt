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
    val full_name: String
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
    val full_name: String? = null,
    @SerializedName("profile_completed")
    val profile_completed: Boolean = false,
    @SerializedName("kyc_status")
    val kyc_status: String? = null
) {
    fun toDomain(): UserProfile {
        return UserProfile(
            id = id,
            email = email,
            fullName = full_name,
            profileCompleted = profile_completed,
            kycStatus = kyc_status
        )
    }
}

data class AuthPayloadDto(
    @SerializedName("token")
    val token: String? = null,
    @SerializedName("access_token")
    val access_token: String? = null,
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