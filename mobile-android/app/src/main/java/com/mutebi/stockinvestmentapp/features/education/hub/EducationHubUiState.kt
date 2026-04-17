package com.mutebi.stockinvestmentapp.features.education.hub

import com.mutebi.stockinvestmentapp.domain.model.EducationArticle

data class EducationHubUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val query: String = "",
    val selectedTopic: String = "All",
    val articles: List<EducationArticle> = emptyList(),
    val visibleArticles: List<EducationArticle> = emptyList(),
    val topics: List<String> = listOf("All")
)