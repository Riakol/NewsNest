package com.riakol.data.remote.dto

import com.google.gson.annotations.SerializedName

data class GuardianApiResponse(
    @SerializedName("response")
    val response: GuardianSearchResponse
)