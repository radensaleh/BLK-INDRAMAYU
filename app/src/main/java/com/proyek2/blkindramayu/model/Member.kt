package com.proyek2.blkindramayu.model

data class Member(
    val kd_pengguna : Int? = null,
    val ktp : Int? = null,
    val nama_lengkap : String? = null,
    val tempat_lahir : String? = null,
    val tgl_lahir : String? = null,
    val jenis_kelamin : String? = null,
    val pend_terakhir : String? = null,
    val thn_ijazah : Int? = null,
    val alamat_lengkap : String? = null,
    val provinsi : String? = null,
    val kabupaten_kota : String? = null,
    val desa_kelurahan : String? = null,
    val rt : Int? = null,
    val rw : Int? = null,
    val kodepos : Int? = null,
    val nomor_kontak : Int? = null,
    val ukuran_baju : String? = null,
    val ukuran_sepatu : Int? = null,
    val username : String? = null,
    val password : String? = null,
    val email : String? = null
)