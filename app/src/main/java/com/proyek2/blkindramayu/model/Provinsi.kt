package com.proyek2.blkindramayu.model

data class Provinsi(
    val id_provinsi : Int? = null,
    val provinsi : String? = null,
    val listkota : List<Kota>? = null
)