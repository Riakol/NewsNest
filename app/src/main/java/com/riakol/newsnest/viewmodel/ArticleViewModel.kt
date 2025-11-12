package com.riakol.newsnest.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riakol.data.util.ApiConfig
import com.riakol.domain.usecase.GetArticleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class ArticleUiState {
    object Loading : ArticleUiState()
    data class Success(val article: com.riakol.domain.model.NewsItem) : ArticleUiState()
    data class Error(val message: String) : ArticleUiState()
}

@HiltViewModel
class ArticleViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getArticleUseCase: GetArticleUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ArticleUiState>(ArticleUiState.Loading)
    val uiState: StateFlow<ArticleUiState> = _uiState

    fun loadArticle(articleId: String) {
        if (articleId.isEmpty()) {
            _uiState.value = ArticleUiState.Error("Invalid article ID")
            return
        }
        viewModelScope.launch {
            getArticleUseCase(
                apiKey = ApiConfig.GUARDIAN_API_KEY,
                articleId = articleId
            ).onSuccess { article ->
                _uiState.value = ArticleUiState.Success(article)
            }.onFailure { e ->
                _uiState.value = ArticleUiState.Error(e.message ?: "Error loading article")
            }
        }
    }
}