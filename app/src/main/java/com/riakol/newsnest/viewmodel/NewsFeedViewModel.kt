package com.riakol.newsnest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riakol.data.util.ApiConfig
import com.riakol.domain.model.NewsItem
import com.riakol.domain.usecase.GetNewsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class NewsUiState {
    object Loading : NewsUiState()
    data class Success(val news: List<NewsItem>) : NewsUiState()
    data class Error(val message: String) : NewsUiState()
}

@HiltViewModel
class NewsFeedViewModel @Inject constructor(
    private val getNewsUseCase: GetNewsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<NewsUiState>(NewsUiState.Loading)
    val uiState: StateFlow<NewsUiState> = _uiState.asStateFlow()

    init {
        loadNews(section = "world")
    }

    fun loadNews(section: String? = null, query: String? = null) {
        viewModelScope.launch {
            _uiState.value = NewsUiState.Loading

            getNewsUseCase(
                apiKey = ApiConfig.GUARDIAN_API_KEY,
                section = section,
                query = query
            ).onSuccess { newsList ->
                _uiState.value = NewsUiState.Success(newsList)
            }.onFailure { exception ->
                _uiState.value = NewsUiState.Error(exception.message ?: "Не удалось загрузить новости")
            }
        }
    }
}