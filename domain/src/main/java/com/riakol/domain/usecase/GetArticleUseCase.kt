package com.riakol.domain.usecase

import com.riakol.domain.repository.NewsRepository
import jakarta.inject.Inject

class GetArticleUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(
        apiKey: String,
        articleId: String
    ) = repository.getArticleById(apiKey, articleId)
}