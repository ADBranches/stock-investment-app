package com.mutebi.stockinvestmentapp.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.mutebi.stockinvestmentapp.domain.model.KycProfile

data class SubmitKycRequestDto(
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("national_id_number")
    val nationalIdNumber: String,
    @SerializedName("document_type")
    val documentType: String,
    @SerializedName("document_number")
    val documentNumber: String
)

data class KycProfileDto(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("user_id")
    val userId: Int? = null,
    @SerializedName("first_name")
    val firstName: String? = null,
    @SerializedName("last_name")
    val lastName: String? = null,
    @SerializedName("national_id_number")
    val nationalIdNumber: String? = null,
    @SerializedName("document_type")
    val documentType: String? = null,
    @SerializedName("document_number")
    val documentNumber: String? = null,
    @SerializedName("status")
    val status: String? = "not_started"
) {
    fun toDomain(): KycProfile {
        return KycProfile(
            id = id,
            userId = userId,
            firstName = firstName.orEmpty(),
            lastName = lastName.orEmpty(),
            nationalIdNumber = nationalIdNumber.orEmpty(),
            documentType = documentType.orEmpty(),
            documentNumber = documentNumber.orEmpty(),
            status = status ?: "not_started"
        )
    }
}

/**
 * Keep temporarily if current endpoint still returns:
 * { success, message, kyc: {...} }
 */
data class KycResponseDto(
    @SerializedName("success")
    val success: Boolean = false,
    @SerializedName("message")
    val message: String = "",
    @SerializedName("kyc")
    val kyc: KycProfileDto? = null
)