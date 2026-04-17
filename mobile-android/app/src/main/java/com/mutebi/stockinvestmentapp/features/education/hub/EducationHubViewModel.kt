package com.mutebi.stockinvestmentapp.features.education.hub

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mutebi.stockinvestmentapp.core.utils.Resource
import com.mutebi.stockinvestmentapp.data.remote.network.RetrofitProvider
import com.mutebi.stockinvestmentapp.data.repository.EducationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EducationHubViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = EducationRepository(
        RetrofitProvider.educationApi(application)
    )

    private val _uiState = MutableStateFlow(EducationHubUiState(isLoading = true))
    val uiState: StateFlow<EducationHubUiState> = _uiState

    init {
        loadArticles()
    }

    fun loadArticles() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            when (val result = repository.getArticles()) {
                is Resource.Success -> {
                    val items = result.data.orEmpty()
                    val topics = listOf("All") + items.map { it.topic }.filter { it.isNotBlank() }.distinct().sorted()

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        articles = items,
                        topics = topics
                    )
                    applyFilters()
                }
                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
                else -> {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
            }
        }
    }

    fun onQueryChange(value: String) {
        _uiState.value = _uiState.value.copy(query = value)
        applyFilters()
    }

    fun onTopicChange(value: String) {
        _uiState.value = _uiState.value.copy(selectedTopic = value)
        applyFilters()
    }

    private fun applyFilters() {
        val current = _uiState.value
        val query = current.query.trim().lowercase()

        val filtered = current.articles.filter { article ->
            val matchesQuery = query.isBlank() ||
                article.title.lowercase().contains(query) ||
                article.summary.lowercase().contains(query) ||
                article.content.lowercase().contains(query)

            val matchesTopic = current.selectedTopic == "All" || article.topic == current.selectedTopic

            matchesQuery && matchesTopic
        }

        _uiState.value = current.copy(visibleArticles = filtered)
    }
}