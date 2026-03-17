package com.mutebi.stockinvestmentapp.data.repository

import com.mutebi.stockinvestmentapp.core.utils.Resource
import com.mutebi.stockinvestmentapp.data.remote.api.KycApi
import com.mutebi.stockinvestmentapp.data.remote.dto.SubmitKycRequestDto
import com.mutebi.stockinvestmentapp.domain.model.KycProfile

class KycRepository(
    private val kycApi: KycApi
) {

    suspend fun getMyKyc(): Resource<KycProfile> {
        return try {
            val response = kycApi.getMyKyc()
            val body = response.body()
            val dto = body?.data

            if (response.isSuccessful && body?.success == true && dto != null) {
                Resource.Success(dto.toDomain())
            } else {
                Resource.Error(body?.message ?: "Failed to load KYC")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to load KYC")
        }
    }

    suspend fun submitKyc(
        firstName: String,
        lastName: String,
        nationalId: String,
        documentType: String,
        documentNumber: String
    ): Resource<KycProfile> {
        return try {
            val response = kycApi.submitKyc(
                SubmitKycRequestDto(
                    firstName = firstName,
                    lastName = lastName,
                    nationalIdNumber = nationalId,
                    documentType = documentType,
                    documentNumber = documentNumber
                )
            )

            val body = response.body()
            val dto = body?.data

            if (response.isSuccessful && body?.success == true && dto != null) {
                Resource.Success(dto.toDomain())
            } else {
                Resource.Error(body?.message ?: "Failed to submit KYC")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to submit KYC")
        }
    }

    suspend fun updateKyc(
        firstName: String,
        lastName: String,
        nationalId: String,
        documentType: String,
        documentNumber: String
    ): Resource<KycProfile> {
        return try {
            val response = kycApi.updateKyc(
                SubmitKycRequestDto(
                    firstName = firstName,
                    lastName = lastName,
                    nationalIdNumber = nationalId,
                    documentType = documentType,
                    documentNumber = documentNumber
                )
            )

            val body = response.body()
            val dto = body?.data

            if (response.isSuccessful && body?.success == true && dto != null) {
                Resource.Success(dto.toDomain())
            } else {
                Resource.Error(body?.message ?: "Failed to update KYC")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to update KYC")
        }
    }
}