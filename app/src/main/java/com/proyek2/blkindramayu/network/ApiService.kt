package com.proyek2.blkindramayu.network

import com.proyek2.blkindramayu.model.*
import com.proyek2.blkindramayu.weather.Result
import retrofit2.Call
import retrofit2.http.*
import retrofit2.http.Url
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming



interface ApiService {

    @GET("getBerita")
    fun getBerita(): Call<Info>

    @GET("getPengumuman")
    fun getPengumuman(): Call<Info>

    @GET("getLoker")
    fun getLoker(): Call<Info>

    @GET("getSemuaBerita")
    fun getSemuaBerita(): Call<Info>

    @GET("getSemuaPengumuman")
    fun getSemuaPengumuman(): Call<Info>

    @GET("getSemuaLoker")
    fun getSemuaLoker(): Call<Info>

    @GET("getPoster")
    fun getPoster(): Call<Poster>

    @GET("getKota")
    fun getKota(): Call<List<Kota>>

    @GET("getProvinsi")
    fun getProvinsi(): Call<List<Provinsi>>

    @GET("getProfilLembaga")
    fun getProfilLembaga(): Call<ProfilLembaga>

    @GET("getMinat")
    fun getMinat(): Call<Minat>

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
        @Field("status") status : Int,
        @Field("listMinat[]") listMinat : ArrayList<Int>
    ) : Call<Res>

    @FormUrlEncoded
    @POST("loginMember")
    fun loginMember(
        @Field("username") username : String,
        @Field("password") password : String
    ) : Call<Res>

    @FormUrlEncoded
    @POST("updateToken")
    fun updateToken(
        @Field("username") username: String,
        @Field("token") token : String?,
        @Field("id") id : Int
    ) : Call<Res>

    @FormUrlEncoded
    @POST("getMember")
    fun getMember(
        @Field("kd_pengguna") kdPengguna: Int,
        @Field("username") username: String
    ) : Call<Res>

    @FormUrlEncoded
    @POST("updateDataDiri")
    fun updateDataDiri(
        @Field("nomor_ktp") nik : String,
        @Field("username") username : String,
        @Field("nama_lengkap") nama : String,
        @Field("tempat_lahir") tmptLahir : Int,
        @Field("tgl_lahir") tglLahir : String,
        @Field("jenis_kelamin") jk : String,
        @Field("pend_terakhir") pend : String,
        @Field("thn_ijazah") thnLulus : Int,
        @Field("nomor_kontak") noKontak : String,
        @Field("email") email : String
    ) : Call<Res>

    @FormUrlEncoded
    @POST("updateUkuran")
    fun updateUkuran(
        @Field("username") username: String,
        @Field("ukuran_baju") baju : String,
        @Field("ukuran_sepatu") sepatu : Int
    ) : Call<Res>

    @FormUrlEncoded
    @POST("updateAlamat")
    fun updateAlamat(
        @Field("username") username: String,
        @Field("alamat_lengkap") alamat: String,
        @Field("provinsi") provinsi : Int,
        @Field("kabupaten_kota") kota : Int,
        @Field("kodepos") kodepos : Int
    ) : Call<Res>

    @FormUrlEncoded
    @POST("updatePassword")
    fun updatePassword(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("password_baru") passwordBaru : String
    ) : Call<Res>

    @FormUrlEncoded
    @POST("updateMinat")
    fun updateMinat(
        @Field("kd_pengguna") kdPengguna: Int,
        @Field("listMinat[]") listMinat: ArrayList<Int>
    ) : Call<Res>

    @FormUrlEncoded
    @POST("getDetailInfo")
    fun getDetailInfo(
        @Field("kd_konten") kd_konten : String,
        @Field("tipe") tipe : Int
    ) : Call<Info>

    @FormUrlEncoded
    @POST("getCurrentWeather")
    fun getCurrentWeather(
        @Field("city") kota : String
    ): Call<Result>

    @FormUrlEncoded
    @POST("getMinatByMember")
    fun getMinatByMember(
        @Field("kd_pengguna") kdPengguna : Int
    ): Call<Minat>

    @FormUrlEncoded
    @POST("getLokerByMinat")
    fun getLokerByMinat(
        @Field("listMinat[]") listMinat: ArrayList<Int>
    ): Call<Info>

    @FormUrlEncoded
    @POST("getSemuaLokerByMinat")
    fun getSemuaLokerByMinat(
        @Field("listMinat[]") listMinat: ArrayList<Int>
    ): Call<Info>

    @GET("getPelatihan")
    fun getPelatihan(): Call<Pelatihan>

    @FormUrlEncoded
    @POST("daftarPelatihan")
    fun daftarPelatihan(
        @Field("kd_skema") kdSkema : Int,
        @Field("kd_pengguna") kdPenguna : Int
    ) : Call<Res>

    @FormUrlEncoded
    @POST("getPelatihanByMember")
    fun getPelatihanByMember(
        @Field("kd_pengguna") kdPengguna: Int
    ) : Call<Pelatihan>

    @FormUrlEncoded
    @POST("getSertifikatMember")
    fun getSertifikatMember(
        @Field("kd_pendaftaran") kdPendaftaran : Int
    ) : Call<Sertifikat>

    @Streaming
    @GET
    fun downloadFileWithDynamicUrlAsync(@Url fileUrl: String): Call<ResponseBody>
}