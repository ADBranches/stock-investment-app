package com.mutebi.stockinvestmentapp.domain.model

data class EducationArticle(
    val id: Int = 0,
    val title: String = "",
    val summary: String = "",
    val content: String = "",
    val topic: String = "",
    val riskWarning: String = "",
    val isBeginnerFriendly: Boolean = true
)