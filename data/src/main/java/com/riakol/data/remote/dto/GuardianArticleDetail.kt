package com.riakol.data.remote.dto

import com.google.gson.annotations.SerializedName

data class GuardianArticleDetail(
    @SerializedName("id")
    val id: String,

    @SerializedName("sectionName")
    val sectionName: String,

    @SerializedName("webPublicationDate")
    val webPublicationDate: String,

    @SerializedName("webTitle")
    val webTitle: String,

    @SerializedName("webUrl")
    val webUrl: String,

    @SerializedName("fields")
    val fields: ArticleFields? = null
)