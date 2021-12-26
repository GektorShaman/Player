package com.player.data.database

import android.media.Image
import android.net.Uri

class Song(songTitle : String, songArtist : String, songPath : String, songImageURI: Uri? = null) {
    var title: String = songTitle
    var artist: String = songArtist
    lateinit var album: String
    lateinit var duration: String
    var path: String = songPath

    var imageURI: Uri? = null

    init {
        imageURI = songImageURI
    }
}