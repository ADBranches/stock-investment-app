package com.mutebi.stockinvestmentapp.data.repository

import com.mutebi.stockinvestmentapp.core.utils.Resource
import com.mutebi.stockinvestmentapp.data.remote.api.EducationApi
import com.mutebi.stockinvestmentapp.domain.model.EducationArticle
import javax.inject.Inject

class EducationRepository @Inject constructor(
    private val api: EducationApi
) {
    suspend fun getArticles(
        query: String? = null,
        topic: String? = null
    ): Resource<List<EducationArticle>> {
        return try {
            val response = api.getArticles(query = query, topic = topic)
            val body = response.body()
            val items = body?.data?.items.orEmpty().map { it.toDomain() }

            if (response.isSuccessful && body?.success == true) {
                Resource.Success(items)
            } else {
                Resource.Error(body?.message ?: "Unable to load education articles")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unable to load education articles")
        }
    }

    suspend fun getArticleById(articleId: Int): Resource<EducationArticle> {
        return try {
            val response = api.getArticleById(articleId)
            val body = response.body()
            val dto = body?.data

            if (response.isSuccessful && body?.success == true && dto != null) {
                Resource.Success(dto.toDomain())
            } else {
                Resource.Error(body?.message ?: "Unable to load article")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unable to load article")
        }
    }
}