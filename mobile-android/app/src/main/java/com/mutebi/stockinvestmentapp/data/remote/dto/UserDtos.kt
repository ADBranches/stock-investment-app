package com.mutebi.stockinvestmentapp.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.mutebi.stockinvestmentapp.domain.model.UserProfile

data class UserProfileDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("email")
    val email: String,
    @SerializedName("full_name")
    val fullName: String? = null,
    @SerializedName("phone_number")
    val phoneNumber: String? = null,
    @SerializedName("country")
    val country: String? = null,
    @SerializedName("date_of_birth")
    val dateOfBirth: String? = null,
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
            phoneNumber = phoneNumber.orEmpty(),
            country = country.orEmpty(),
            dateOfBirth = dateOfBirth.orEmpty(),
            profileCompleted = profileCompleted,
            kycStatus = kycStatus.orEmpty()
        )
    }
}

data class UpdateProfileRequestDto(
    @SerializedName("full_name")
    val fullName: String,
    @SerializedName("phone_number")
    val phoneNumber: String? = null,
    @SerializedName("country")
    val country: String? = null,
    @SerializedName("date_of_birth")
    val dateOfBirth: String? = null
)

/**
 * Keep temporarily if current profile endpoint still returns:
 * { success, message, user: {...} }
 */
data class UserProfileResponseDto(
    @SerializedName("success")
    val success: Boolean = false,
    @SerializedName("message")
    val message: String = "",
    @SerializedName("user")
    val user: UserProfileDto? = null
)
