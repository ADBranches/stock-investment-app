package com.mutebi.stockinvestmentapp.data.repository

import com.mutebi.stockinvestmentapp.core.utils.Resource
import com.mutebi.stockinvestmentapp.data.remote.api.UserApi
import com.mutebi.stockinvestmentapp.data.remote.dto.UpdateProfileRequestDto
import com.mutebi.stockinvestmentapp.domain.model.UserProfile

class UserRepository(
    private val userApi: UserApi
) {
    suspend fun getProfile(): Resource<UserProfile> {
        return try {
            val response = userApi.getProfile()
            val body = response.body()
            val dto = body?.data

            if (response.isSuccessful && body?.success == true && dto != null) {
                Resource.Success(
                    UserProfile(
                        id = dto.id,
                        email = dto.email,
                        fullName = dto.fullName.orEmpty(),
                        phoneNumber = dto.phoneNumber.orEmpty(),
                        country = dto.country.orEmpty(),
                        dateOfBirth = dto.dateOfBirth.orEmpty(),
                        profileCompleted = dto.profileCompleted,
                        kycStatus = dto.kycStatus.orEmpty()
                    )
                )
            } else {
                Resource.Error(body?.message ?: "Failed to load profile")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to load profile")
        }
    }

    suspend fun updateProfile(
        fullName: String,
        phoneNumber: String,
        country: String,
        dateOfBirth: String
    ): Resource<UserProfile> {
        return try {
            val response = userApi.updateProfile(
                UpdateProfileRequestDto(
                    fullName = fullName,
                    phoneNumber = phoneNumber,
                    country = country,
                    dateOfBirth = dateOfBirth
                )
            )
            val body = response.body()
            val dto = body?.data

            if (response.isSuccessful && body?.success == true && dto != null) {
                Resource.Success(
                    UserProfile(
                        id = dto.id,
                        email = dto.email,
                        fullName = dto.fullName.orEmpty(),
                        phoneNumber = dto.phoneNumber.orEmpty(),
                        country = dto.country.orEmpty(),
                        dateOfBirth = dto.dateOfBirth.orEmpty(),
                        profileCompleted = dto.profileCompleted,
                        kycStatus = dto.kycStatus.orEmpty()
                    )
                )
            } else {
                Resource.Error(body?.message ?: "Failed to update profile")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to update profile")
        }
    }
}