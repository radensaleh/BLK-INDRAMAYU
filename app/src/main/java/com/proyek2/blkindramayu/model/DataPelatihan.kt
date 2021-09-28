package com.proyek2.blkindramayu.model

import com.google.gson.annotations.SerializedName

data class DataPelatihan(

	@field:SerializedName("tgl_akhir_pendaftaran")
	val tglAkhirPendaftaran: String? = null,

	@field:SerializedName("detail_program")
	val detailProgram: String? = null,

	@field:SerializedName("nama_program")
	val namaProgram: String? = null,

	@field:SerializedName("tgl_awal_pelaksanaan")
	val tglAwalPelaksanaan: String? = null,

	@field:SerializedName("kuota")
	val kuota: Int? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("kd_skema")
	val kdSkema: Int? = null,

	@field:SerializedName("kd_gelombang")
	val kdGelombang: Int? = null,

	@field:SerializedName("kd_pendaftaran")
	val kdPendaftaran: Int? = null,

	@field:SerializedName("tgl_akhir_pelaksanaan")
	val tglAkhirPelaksanaan: String? = null,

	@field:SerializedName("kd_program")
	val kdProgram: Int? = null,

	@field:SerializedName("tgl_seleksi")
	val tglSeleksi: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("nama_gelombang")
	val namaGelombang: String? = null,

	@field:SerializedName("tgl_awal_pendaftaran")
	val tglAwalPendaftaran: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)