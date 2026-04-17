package com.mutebi.stockinvestmentapp.features.education.article

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mutebi.stockinvestmentapp.core.utils.Resource
import com.mutebi.stockinvestmentapp.data.remote.network.RetrofitProvider
import com.mutebi.stockinvestmentapp.data.repository.EducationRepository
import com.mutebi.stockinvestmentapp.domain.model.EducationArticle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ArticleDetailUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val article: EducationArticle? = null
)

class ArticleDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = EducationRepository(
        RetrofitProvider.educationApi(application)
    )

    private val _uiState = MutableStateFlow(ArticleDetailUiState(isLoading = true))
    val uiState: StateFlow<ArticleDetailUiState> = _uiState

    fun loadArticle(articleId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            when (val result = repository.getArticleById(articleId)) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        article = result.data
                    )
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
}