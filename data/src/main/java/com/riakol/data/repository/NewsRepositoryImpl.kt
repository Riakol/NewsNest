package com.riakol.data.repository

import com.riakol.data.remote.api.GuardianApiService
import com.riakol.data.remote.mapper.toDomainModel
import com.riakol.domain.model.NewsItem
import com.riakol.domain.repository.NewsRepository
import jakarta.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val apiService: GuardianApiService
) : NewsRepository {

    override suspend fun getNews(
        apiKey: String,
        section: String?,
        query: String?
    ): Result<List<NewsItem>> {
        return try {
            val response = apiService.getArticles(
                apiKey = apiKey,
                section = section,
                query = query,
                pageSize = 20,
                showFields = "headline,trailText,thumbnail"
            )

            if (response.response.status != "ok") {
                return Result.failure(RuntimeException("API returned status: ${response.response.status}"))
            }

            val newsItems = response.response.results.map { it.toDomainModel() }
            Result.success(newsItems)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}