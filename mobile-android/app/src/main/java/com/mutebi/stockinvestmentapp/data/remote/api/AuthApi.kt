package com.mutebi.stockinvestmentapp.data.remote.api

import com.mutebi.stockinvestmentapp.data.remote.dto.ApiEnvelopeDto
import com.mutebi.stockinvestmentapp.data.remote.dto.AuthPayloadDto
import com.mutebi.stockinvestmentapp.data.remote.dto.AuthUserDto
import com.mutebi.stockinvestmentapp.data.remote.dto.ForgotPasswordRequestDto
import com.mutebi.stockinvestmentapp.data.remote.dto.LoginRequestDto
import com.mutebi.stockinvestmentapp.data.remote.dto.RegisterRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApi {
    @POST("api/v1/auth/register")
    suspend fun register(
        @Body request: RegisterRequestDto
    ): Response<ApiEnvelopeDto<AuthPayloadDto>>

    @POST("api/v1/auth/login")
    suspend fun login(
        @Body request: LoginRequestDto
    ): Response<ApiEnvelopeDto<AuthPayloadDto>>

    @POST("api/v1/auth/forgot-password")
    suspend fun forgotPassword(
        @Body request: ForgotPasswordRequestDto
    ): Response<ApiEnvelopeDto<Unit>>

    @GET("api/v1/auth/me")
    suspend fun me(): Response<ApiEnvelopeDto<AuthUserDto>>
}
