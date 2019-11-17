package com.proyek2.blkindramayu.network

import com.proyek2.blkindramayu.model.*
import com.proyek2.blkindramayu.weather.Result
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("getBerita")
    fun getBerita(): Call<Info>

    @GET("getLoker")
    fun getLoker(): Call<Info>

    @GET("getPoster")
    fun getPoster(): Call<Poster>

    @GET("getKota")
    fun getKota(): Call<List<Kota>>

    @GET("getProvinsi")
    fun getProvinsi(): Call<List<Provinsi>>

    @GET("getProfilLembaga")
    fun getProfilLembaga(): Call<ProfilLembaga>

    @FormUrlEncoded
    @POST("registrasiMember")
    fun registrasiMember(
        @Field("nomor_ktp") nik : String,
        @Field("nama_lengkap") nama : String,
        @Field("tempat_lahir") tmptLahir : Int,
        @Field("tgl_lahir") tglLahir : String,
        @Field("jenis_kelamin") jk : String,
        @Field("provinsi") provinsi : Int,
        @Field("kabupaten_kota") kota : Int,
        @Field("alamat_lengkap") alamat : String,
        @Field("kodepos") kodepos : Int,
        @Field("username") username : String,
        @Field("password") password : String,
        @Field("email") email : String,
        @Field("status") status : Int
    ) : Call<Res>

    @FormUrlEncoded
    @POST("loginMember")
    fun loginMember(
        @Field("username") username : String,
        @Field("password") password : String
    ) : Call<Res>

    @FormUrlEncoded
    @POST("getDetailInfo")
    fun getDetailInfo(
        @Field("kd_konten") kd_konten : String
    ) : Call<Info>

    @FormUrlEncoded
    @POST("getCurrentWeather")
    fun getCurrentWeather(
        @Field("city") kota : String
    ): Call<Result>

}