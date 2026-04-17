package com.mutebi.stockinvestmentapp.data.remote.api

import com.mutebi.stockinvestmentapp.data.remote.dto.ApiEnvelopeDto
import com.mutebi.stockinvestmentapp.data.remote.dto.TransactionListPayloadDto
import retrofit2.Response
import retrofit2.http.GET

interface TransactionApi {
    @GET("api/v1/portfolio/transactions")
    suspend fun getTransactions(): Response<ApiEnvelopeDto<TransactionListPayloadDto>>
}