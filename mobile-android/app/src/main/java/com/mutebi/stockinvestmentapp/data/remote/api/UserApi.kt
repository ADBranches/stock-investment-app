package com.mutebi.stockinvestmentapp.data.remote.api

import com.mutebi.stockinvestmentapp.data.remote.dto.ApiEnvelopeDto
import com.mutebi.stockinvestmentapp.data.remote.dto.UpdateProfileRequestDto
import com.mutebi.stockinvestmentapp.data.remote.dto.UserProfileDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH

interface UserApi {
    @GET("api/v1/users/me")
    suspend fun getProfile(): Response<ApiEnvelopeDto<UserProfileDto>>

    @PATCH("api/v1/users/me/profile")
    suspend fun updateProfile(
        @Body request: UpdateProfileRequestDto
    ): Response<ApiEnvelopeDto<UserProfileDto>>
}