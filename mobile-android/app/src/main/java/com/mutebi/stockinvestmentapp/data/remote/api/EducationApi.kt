package com.mutebi.stockinvestmentapp.data.remote.api

import com.mutebi.stockinvestmentapp.data.remote.dto.ApiEnvelopeDto
import com.mutebi.stockinvestmentapp.data.remote.dto.EducationArticleDto
import com.mutebi.stockinvestmentapp.data.remote.dto.EducationListPayloadDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface EducationApi {
    @GET("api/v1/education")
    suspend fun getArticles(
        @Query("q") query: String? = null,
        @Query("topic") topic: String? = null
    ): Response<ApiEnvelopeDto<EducationListPayloadDto>>

    @GET("api/v1/education/{articleId}")
    suspend fun getArticleById(
        @Path("articleId") articleId: Int
    ): Response<ApiEnvelopeDto<EducationArticleDto>>
}  