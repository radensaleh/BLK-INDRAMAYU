package com.proyek2.blkindramayu.dbHelper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

class DBHelper private constructor(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "MyDatabase", null, 1){

    init {
        instance = this
    }

    companion object {
        private var instance: DBHelper? = null

        @Synchronized
        fun getInstance(ctx: Context) = instance ?: DBHelper(ctx.applicationContext)
    }

    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.createTable("member", true,
            "kd_pelanggan" to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
            "nomor_ktp" to INTEGER + NOT_NULL, "nama_lengkap" to TEXT, "tempat_lahir" to TEXT, "tgl_lahir" to TEXT,
            "jenis_kelamin" to TEXT, "pend_terakhir" to TEXT, "thn_ijazah" to TEXT, "alamat_lengkap" to TEXT, "provinsi" to TEXT,
            "kabupaten_kota" to TEXT, "desa_kelurahan" to TEXT, "rt" to INTEGER, "rw" to INTEGER, "kodepos" to INTEGER, "nomor_kontak" to TEXT,
            "ukuran_baju" to TEXT, "ukuran_sepatu" to INTEGER, "username" to TEXT + NOT_NULL, "password" to TEXT + NOT_NULL, "email" to TEXT
        )
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.dropTable("member", true)
    }

    val Context.database : DBHelper
        get() = DBHelper.getInstance(this)

}