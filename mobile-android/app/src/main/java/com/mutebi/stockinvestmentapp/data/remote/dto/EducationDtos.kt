package com.mutebi.stockinvestmentapp.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.mutebi.stockinvestmentapp.domain.model.EducationArticle

data class EducationListPayloadDto(
    @SerializedName("items")
    val items: List<EducationArticleDto> = emptyList()
)

data class EducationArticleDto(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("title")
    val title: String = "",
    @SerializedName("summary")
    val summary: String = "",
    @SerializedName("content")
    val content: String = "",
    @SerializedName("topic")
    val topic: String = "",
    @SerializedName("risk_warning")
    val riskWarning: String? = null,
    @SerializedName("is_beginner_friendly")
    val isBeginnerFriendly: Boolean = true
) {
    fun toDomain(): EducationArticle {
        return EducationArticle(
            id = id,
            title = title,
            summary = summary,
            content = content,
            topic = topic,
            riskWarning = riskWarning.orEmpty(),
            isBeginnerFriendly = isBeginnerFriendly
        )
    }
}