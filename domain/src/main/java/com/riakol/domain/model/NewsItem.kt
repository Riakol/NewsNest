package com.riakol.domain.model

data class NewsItem(
    val id: String,
    val title: String,
    val url: String,
    val imageUrl: String?,
    val publishedAt: String,
    val section: String,
    val trailText: String?,
    val body: String? = null,
)