package com.proyek2.blkindramayu.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

@Entity(tableName = "member")
data class MemberEntity (
    @NotNull @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Int,
    @NotNull @ColumnInfo(name = "kd_pengguna") var kd_pengguna : Int,
    @NotNull @ColumnInfo(name = "nomor_ktp") var nomor_ktp : String,
    @NotNull @ColumnInfo(name = "nama_lengkap") var nama_lengkap : String,
    @NotNull @ColumnInfo(name = "tempat_lahir") var tempat_lahir : String,
    @NotNull @ColumnInfo(name = "tgl_lahir") var tgl_lahir : String,
    @NotNull @ColumnInfo(name = "jenis_kelamin") var jenis_kelamin : String,
    @NotNull @ColumnInfo(name = "alamat_lengkap") var alamat_lengkap : String,
    @NotNull @ColumnInfo(name = "provinsi") var provinsi : String,
    @NotNull @ColumnInfo(name = "kabupaten_kota") var kabupaten_kota : String,
    @NotNull @ColumnInfo(name = "kodepos") var kodepos : Int,
    @Nullable @ColumnInfo(name = "pend_terakhir") var pend_terakhir : String?,
    @Nullable @ColumnInfo(name = "thn_ijazah") var thn_ijazah : Int?,
    @Nullable @ColumnInfo(name = "nomor_kontak") var nomor_kontak : String?,
    @Nullable @ColumnInfo(name = "ukuran_baju") var ukuran_baju : String?,
    @Nullable @ColumnInfo(name = "ukuran_sepatu") var ukuran_sepatu : Int?,
    @NotNull @ColumnInfo(name = "username") var username : String,
    @NotNull @ColumnInfo(name = "password") var password : String,
    @NotNull @ColumnInfo(name = "email") var email : String
    //@NotNull @ColumnInfo(name = "status") var status : Int
)
