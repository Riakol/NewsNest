package com.riakol.data.remote.mapper

import com.riakol.data.remote.dto.GuardianArticle
import com.riakol.domain.model.NewsItem

fun GuardianArticle.toDomainModel(): NewsItem = NewsItem(
    id = id,
    title = webTitle,
    url = webUrl.trim(),
    imageUrl = fields?.thumbnail?.trim(),
    publishedAt = webPublicationDate,
    section = sectionName,
    trailText = fields?.trailText
)