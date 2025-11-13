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
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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

    val topics = listOf(
        "politics",
        "environment",
        "society",
        "education",
        "media",
        "film",
        "culture",
        "books",
        "music",
        "travel",
        "life and style",
        "football",
        "cricket",
        "tennis",
        "opinion",
        "guardian view",
        "obituaries",
        "features"
    )

    private val _selectedTopic = MutableStateFlow<String?>(topics.first())
    val selectedTopic: StateFlow<String?> = _selectedTopic.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        loadNews(section = _selectedTopic.value)

        _searchQuery
            .debounce(500L)
            .distinctUntilChanged()
            .onEach { query ->
                if (query.length > 2) {
                    loadNews(section = null, query = query)
                } else if (query.isEmpty()) {
                    if (_selectedTopic.value == null) {
                        onTopicChange(topics.first())
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    /**
     * It is triggered when text is entered into the search bar.
     */
    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        if (query.isNotEmpty()) {
            _selectedTopic.value = null
        }
    }

    /**
     * It is triggered when clicking on the topic chip.
     */
    fun onTopicChange(topic: String) {
        _selectedTopic.value = topic
        _searchQuery.value = ""
        loadNews(section = topic, query = null)
    }

    /**
     * It is triggered when the "Search" button is pressed on the keyboard.
     */
    fun onSearchSubmit() {
        val query = _searchQuery.value
        if (query.length > 2) {
            loadNews(section = null, query = query)
        }
    }


    /**
     * Private method for loading news.
     */
    private fun loadNews(section: String? = null, query: String? = null) {
        viewModelScope.launch {
            _uiState.value = NewsUiState.Loading

            val effectiveQuery = query?.takeIf { it.isNotBlank() }
            val effectiveSection = section?.takeIf { it.isNotBlank() }

            if (effectiveQuery == null && effectiveSection == null) {
                _uiState.value = NewsUiState.Error("Invalid request: no section or query.")
            }

            getNewsUseCase(
                apiKey = ApiConfig.GUARDIAN_API_KEY,
                section = effectiveSection,
                query = effectiveQuery
            ).onSuccess { newsList ->
                _uiState.value = NewsUiState.Success(newsList)
            }.onFailure { exception ->
                _uiState.value = NewsUiState.Error(exception.message ?: "Failed to load news.")
            }
        }
    }
}