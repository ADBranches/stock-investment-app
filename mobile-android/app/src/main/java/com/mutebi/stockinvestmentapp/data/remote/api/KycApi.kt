package com.mutebi.stockinvestmentapp.data.remote.api

import com.mutebi.stockinvestmentapp.data.remote.dto.ApiEnvelopeDto
import com.mutebi.stockinvestmentapp.data.remote.dto.KycProfileDto
import com.mutebi.stockinvestmentapp.data.remote.dto.SubmitKycRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST

interface KycApi {
    @GET("api/v1/kyc/me")
    suspend fun getMyKyc(): Response<ApiEnvelopeDto<KycProfileDto>>

    @POST("api/v1/kyc/submit")
    suspend fun submitKyc(
        @Body request: SubmitKycRequestDto
    ): Response<ApiEnvelopeDto<KycProfileDto>>

    @PATCH("api/v1/kyc/me")
    suspend fun updateKyc(
        @Body request: SubmitKycRequestDto
    ): Response<ApiEnvelopeDto<KycProfileDto>>
}