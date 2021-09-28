package com.proyek2.blkindramayu.model

import com.google.gson.annotations.SerializedName

data class Pelatihan(

	@field:SerializedName("status_code")
	val statusCode: Int? = null,

	@field:SerializedName("data")
	val data: List<DataPelatihan>? = null,

	@field:SerializedName("message")
	val message: String? = null
)