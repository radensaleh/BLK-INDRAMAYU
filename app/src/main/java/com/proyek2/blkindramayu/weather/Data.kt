package com.proyek2.blkindramayu.weather

data class Data(
    val timezone : String? = null,
    val city_name : String? = null,
    val weather : Weather? = null,
    val temp : Double? = null,
    val app_temp : Double? = null
)