package com.proyek2.blkindramayu.network

import com.proyek2.blkindramayu.model.Info
import com.proyek2.blkindramayu.model.Poster
import com.proyek2.blkindramayu.model.ProfilLembaga
import com.proyek2.blkindramayu.model.Res
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("getBerita")
    fun getBerita(): Call<Info>

    @GET("getLoker")
    fun getLoker(): Call<Info>

    @GET("getPoster")
    fun getPoster(): Call<Poster>

    @GET("getProfilLembaga")
    fun getProfilLembaga(): Call<ProfilLembaga>

    @FormUrlEncoded
    @POST("loginMember")
    fun loginMember(
        @Field("username") username : String,
        @Field("password") password : String
    ) : Call<Res>

}