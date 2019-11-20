package com.proyek2.blkindramayu.activity

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.proyek2.blkindramayu.BuildConfig
import com.proyek2.blkindramayu.R
import com.proyek2.blkindramayu.model.DataInfo
import com.proyek2.blkindramayu.viewModel.DetailViewModel
import kotlinx.android.synthetic.main.activity_detail_info.*

@Suppress("DEPRECATION", "UNCHECKED_CAST")
class DetailInfoActivity : AppCompatActivity() {

    private var id : String? = null
    private var judul : String? = null
    private var kategoriKonten : String? = null
    private var isiKonten : String? = null
    private var foto : String? = null
    private var tglUpload : String? = null

    private lateinit var viewModelDetailInfo : DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_info)

        val intent = intent

        id = intent.getStringExtra("id")

        setData()

    }

    @SuppressLint("SetTextI18n")
    private fun setData(){

        viewModelDetailInfo = ViewModelProviders.of(this).get(DetailViewModel::class.java)
        id?.let {
            viewModelDetailInfo.setData(it).observe(this, Observer {
                t ->
                showData(t?.data as List<DataInfo>)
            })
        }

    }

    @SuppressLint("SetTextI18n")
    private fun showData(data: List<DataInfo>){
        judul = data[0].judul
        kategoriKonten = data[0].kategori_konten
        tglUpload = data[0].tgl_upload
        isiKonten = data[0].isi_konten
        foto = data[0].foto

        Glide.with(this)
            .load("${BuildConfig.IMAGE}/konten/$foto")
            .into(imgInfo)

        tvTitle.text = "Detail $kategoriKonten"
        tvJudul.text = judul
        tvKategori.text = kategoriKonten
        tvTglUpload.text = "Diunggah pada : $tglUpload"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvDeskripsi.text = Html.fromHtml(isiKonten, Html.FROM_HTML_MODE_COMPACT)
        } else {
            tvDeskripsi.text = Html.fromHtml(isiKonten)
        }

    }
}
