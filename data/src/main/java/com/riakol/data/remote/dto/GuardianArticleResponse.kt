package com.riakol.data.remote.dto

import com.google.gson.annotations.SerializedName

data class GuardianArticleResponse(
    @SerializedName("status")
    val status: String,

    @SerializedName("content")
    val content: GuardianArticleDetail
)