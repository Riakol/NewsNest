package com.riakol.domain.repository

import com.riakol.domain.model.NewsItem

interface NewsRepository {
    suspend fun getNews(
        apiKey: String,
        section: String? = null,
        query: String? = null
    ): Result<List<NewsItem>>

    suspend fun getArticleById(
        apiKey: String,
        articleId: String
    ): Result<NewsItem>
}