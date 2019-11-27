package com.proyek2.blkindramayu.model

data class Res(
    val status_code : Int? = null,
    val message  : List<String?>? = null,
    val data_member : List<Member?>? = null,
    val minat_member : List<DataMinat>? = null
)