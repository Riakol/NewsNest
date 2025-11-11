package com.riakol.domain.usecase

import com.riakol.domain.repository.NewsRepository
import jakarta.inject.Inject

class GetNewsUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(
        apiKey: String,
        section: String? = null,
        query: String? = null
    ) = repository.getNews(apiKey, section, query)
}