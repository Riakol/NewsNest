package com.riakol.data.remote.dto

import com.google.gson.annotations.SerializedName

data class GuardianDetailApiResponse(
    @SerializedName("response")
    val response: GuardianArticleResponse
)