package com.riakol.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ArticleFields(
    @SerializedName("headline")
    val headline: String? = null,

    @SerializedName("trailText")
    val trailText: String? = null,

    @SerializedName("thumbnail")
    val thumbnail: String? = null,

    @SerializedName("body")
    val body: String? = null
)