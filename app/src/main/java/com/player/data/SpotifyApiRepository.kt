package com.player.data

import android.util.Log
import com.player.SpotifyResponseModel
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import android.util.Base64
import com.player.data.database.SpotifyAuthorizationResponseModel
import com.player.SpotifySongResponseModel


class SpotifyApiRepository {

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("https://api.spotify.com/v1/")
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    private val spotifyApi: SpotifyApi = retrofit.create(SpotifyApi::class.java)

    fun SongList(ids: String, token: String): Observable<SpotifyResponseModel> {
        Log.d("LOG", "SongLIST")
        return spotifyApi.SongList(ids, "Bearer BQACr9rWVqmqGlfNRRPtSIPJg7Frq9sIiXPr7uuK8RDcM5m5o9qjh6rteDzHhCgo3Oqnk4Ubod9_0h603TQ")
    }

    fun Song(id:String, token: String): Observable<SpotifySongResponseModel> {
        Log.d("LOG", "Song")
        return spotifyApi.SingleSong(id, "Bearer BQACr9rWVqmqGlfNRRPtSIPJg7Frq9sIiXPr7uuK8RDcM5m5o9qjh6rteDzHhCgo3Oqnk4Ubod9_0h603TQ")
    }

    fun Authorise(
        client_id: String,
        client_secret: String
    ): Observable<SpotifyAuthorizationResponseModel> {
        lateinit var s: Observable<SpotifyAuthorizationResponseModel>
        val param = HashMap<String, String>()
        param["grant_type"] = "Basic " + Base64.encodeToString(
            "$client_id:$client_secret".toByteArray(),
            Base64.NO_WRAP
        )
        Log.d("LOG",param["grant_type"].toString())
        Log.d("LOG",param.toString())
        return spotifyApi.Authorise(
            "client_credentials",
            param
        )
    }
}