package com.player.data

import com.player.SpotifyResponseModel
import com.player.data.database.SpotifyAuthorizationResponseModel
import com.player.SpotifySongResponseModel
//import io.reactivex.Observable
import io.reactivex.Observable
import retrofit2.http.*

interface SpotifyApi {

        @GET("tracks")
        fun SongList(@Query("ids") ids: String,@Header("Authorization") token: String) : Observable<SpotifyResponseModel>

        @GET("tracks/{id}")
        fun SingleSong(@Path("id") id: String,@Header("Authorization") token: String) : Observable<SpotifySongResponseModel>

        //@Headers("Content-Type: application/x-www-form-urlencoded")
        @FormUrlEncoded
        @POST("https://accounts.spotify.com/api/token")
        fun Authorise(
                @Header("Authorization") auth : String,
                //@Body grant_type: GrantTypeBody,
                @FieldMap grant_type: HashMap<String, String>
        ) : Observable<SpotifyAuthorizationResponseModel>
}