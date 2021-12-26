package com.player

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SpotifySongResponseModel(
    @Json(name = "album")
    val album: Album,
    @Json(name = "artists")
    val artists: List<Artist>,
    @Json(name = "duration_ms")
    val durationMS: Int,
    @Json(name = "name")
    val name: String,
    @Json(name = "preview_url")
    val previewURL: String,
)

@JsonClass(generateAdapter = true)
data class Album(
    @Json(name = "images")
    val images: List<Image>,
)

@JsonClass(generateAdapter = true)
data class Artist(
    @Json(name = "name")
    val name: String,
)

@JsonClass(generateAdapter = true)
data class Image(
    @Json(name = "height")
    val height: Long,
    @Json(name = "url")
    val url: String,
    @Json(name = "width")
    val width: Long
)
