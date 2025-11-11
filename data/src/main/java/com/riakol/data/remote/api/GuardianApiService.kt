package com.riakol.data.remote.api

import com.riakol.data.remote.dto.GuardianApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GuardianApiService {
    @GET("search")
    suspend fun getArticles(
        @Query("api-key") apiKey: String,
        @Query("section") section: String? = null,
        @Query("q") query: String? = null,
        @Query("page-size") pageSize: Int = 10,
        @Query("show-fields") showFields: String = "headline,trailText,thumbnail",
        @Query("order-by") orderBy: String = "newest"
    ): GuardianApiResponse
}