package com.proyek2.blkindramayu.model

data class Member(
    val kd_pengguna : Int? = null,
    val nomor_ktp : String? = null,
    val nama_lengkap : String? = null,
    val tempat_lahir : String? = null,
    val id_kotalahir : Int? = null,
    val tgl_lahir : String? = null,
    val jenis_kelamin : String? = null,
    val alamat_lengkap : String? = null,
    val provinsi : String? = null,
    val id_provinsi : Int? = null,
    val kabupaten_kota : String? = null,
    val id_kota : Int? = null,
    val kodepos : Int? = null,
    val pend_terakhir : String? = null,
    val thn_ijazah : Int? = null,
    val nomor_kontak : String? = null,
    val ukuran_baju : String? = null,
    val ukuran_sepatu : Int? = null,
    val username : String? = null,
    val password : String? = null,
    val email : String? = null,
    val status : Int? = null
)