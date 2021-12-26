package com.player

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SpotifyResponseModel(
    @Json(name = "tracks")
    val tracks: List<Track>
)

@JsonClass(generateAdapter = true)
data class Track(
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
