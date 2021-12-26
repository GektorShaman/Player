package com.player.data.database

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SpotifyAuthorizationResponseModel(
    @Json(name = "access_token")
    val token: String,
    @Json(name = "token_type")
    val type: String,
    @Json(name = "expires_in")
    val expires: Int
)
