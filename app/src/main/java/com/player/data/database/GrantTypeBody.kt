package com.player.data.database

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GrantTypeBody(
    @Json(name = "grant_type")
    val grant_type: String
    )