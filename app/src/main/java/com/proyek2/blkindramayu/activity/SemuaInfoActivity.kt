package com.proyek2.blkindramayu.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.proyek2.blkindramayu.R
import com.proyek2.blkindramayu.adapter.AdapterInfo
import com.proyek2.blkindramayu.model.DataInfo
import com.proyek2.blkindramayu.model.Info
import com.proyek2.blkindramayu.model.Minat
import com.proyek2.blkindramayu.network.NetworkConfig
import com.proyek2.blkindramayu.room.AppDataBase
import com.proyek2.blkindramayu.room.MemberEntity
import kotlinx.android.synthetic.main.activity_semua_info.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Suppress("UNCHECKED_CAST")
class SemuaInfoActivity : AppCompatActivity() {

    private var info : Int? = null
    private var member : MemberEntity? = null
    private var appDB : AppDataBase? = null
    private var kdPengguna : Int? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_semua_info)

        val intent = intent
        info = intent.getIntExtra("info", 0)

        appDB = AppDataBase.getInstance(this)
        member = appDB?.memberDao()?.getMember()
        kdPengguna = appDB?.memberDao()?.getMember()?.kd_pengguna

        instance()

        refreshLayout.setOnRefreshListener { instance() }

    }

    @SuppressLint("SetTextI18n", "ResourceAsColor")
    private fun instance(){
        val leftRight = AnimationUtils.loadAnimation(this, R.anim.lefttoright)
        val rightLeft = AnimationUtils.loadAnimation(this, R.anim.righttoleft)
        val downToup  = AnimationUtils.loadAnimation(this, R.anim.downtoup)

        imgHeader.startAnimation(downToup)
        tvHeader.startAnimation(leftRight)
        tvCari.startAnimation(leftRight)
        searchView.startAnimation(leftRight)
        //btnDate.startAnimation(rightLeft)
        rvSemuaInfo.startAnimation(downToup)

        searchView.queryHint = "Judul/Tanggal"
        searchView.setQuery("", false)

        when (info) {
            1 -> {
                tvHeader.text = " SEMUA BERITA"
                tvCari.text = " CARI BERITAMU SEKARANG!"
                Glide.with(this)
                    .load(R.drawable.beritabaru)
                    .into(imgHeader)
                getSemuaBerita()
            }
            2 -> {
                tvHeader.text = " REKOMENDASI LOKER"
                tvCari.text = " CARI LOKERMU SEKARANG!"
                Glide.with(this)
                    .load(R.drawable.rekomended)
                    .into(imgHeader)
                if(member != null){ kdPengguna?.let { getMinatMember(it) } }

            }
            3 -> {
                tvHeader.text = " SEMUA LOKER"
                tvCari.text = " CARI LOKERMU SEKARANG!"
                Glide.with(this)
                    .load(R.drawable.loker)
                    .into(imgHeader)
                getSemuaLoker()
            } else -> {
                tvHeader.text = " SEMUA PENGUMUMAN"
                tvCari.text = " CARI PENGUMUMANMU SEKARANG!"
                Glide.with(this)
                    .load(R.drawable.pengumuman)
                    .into(imgHeader)
                getSemuaPengumuman()
            }
        }
    }

    private fun getMinatMember(kdPengguna : Int){
        NetworkConfig().api().getMinatByMember(kdPengguna).enqueue(object : Callback<Minat>{
            override fun onFailure(call: Call<Minat>, t: Throwable) {
                Toast.makeText(this@SemuaInfoActivity, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Minat>, response: Response<Minat>) {
                if (response.isSuccessful){
                    val data = response.body()?.data
                    val listMinat = ArrayList<Int>()

                    for(i in data!!.indices){ data[i]?.id_minat?.let { listMinat.add(it) } }
                    getSemuaLokerByMinat(listMinat)
                }else{
                    Toast.makeText(this@SemuaInfoActivity, response.message(), Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

    private fun getSemuaLokerByMinat(listMinat : ArrayList<Int>){
        NetworkConfig().api().getSemuaLokerByMinat(listMinat).enqueue(object : Callback<Info>{
            override fun onFailure(call: Call<Info>, t: Throwable) {
                imgKosong.visibility = View.GONE
                tvDataKosong.visibility = View.GONE
                refreshLayout.isRefreshing = false
                Toast.makeText(this@SemuaInfoActivity, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Info>, response: Response<Info>) {
                refreshLayout.isRefreshing = false
                if(response.isSuccessful){
                    val data = response.body()?.data
                    if(data?.isEmpty()!!){
                        imgKosong.visibility = View.VISIBLE
                        tvDataKosong.visibility = View.VISIBLE
                    }else{
                        imgKosong.visibility = View.GONE
                        tvDataKosong.visibility = View.GONE
                        rvSemuaInfo.layoutManager = GridLayoutManager(this@SemuaInfoActivity, 2)
                        val adapter =
                            AdapterInfo(data as List<DataInfo>, this@SemuaInfoActivity, 2)
                        rvSemuaInfo.adapter = adapter
                        //adapter.notifyDataSetChanged()

                        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                            androidx.appcompat.widget.SearchView.OnQueryTextListener {
                            override fun onQueryTextSubmit(p0: String?): Boolean {
                                p0?.let { adapter.filter(it) }
                                return false
                            }

                            override fun onQueryTextChange(p0: String?): Boolean {
                                p0?.let { adapter.filter(it) }
                                return false
                            }

                        })
                    }
                }else{
                    Toast.makeText(this@SemuaInfoActivity, response.message(), Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

    private fun getSemuaBerita(){
        NetworkConfig().api().getSemuaBerita().enqueue(object : Callback<Info>{
            override fun onFailure(call: Call<Info>, t: Throwable) {
                tvDataKosong.visibility = View.GONE
                refreshLayout.isRefreshing = false
                Toast.makeText(this@SemuaInfoActivity, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Info>, response: Response<Info>) {
                refreshLayout.isRefreshing = false
                if(response.isSuccessful){
                    val data = response.body()?.data
                    if(data?.isEmpty()!!){
                        imgKosong.visibility = View.VISIBLE
                        tvDataKosong.visibility = View.VISIBLE
                    }else{
                        imgKosong.visibility = View.GONE
                        tvDataKosong.visibility = View.GONE
                        rvSemuaInfo.layoutManager = GridLayoutManager(this@SemuaInfoActivity, 2)
                        val adapter =
                            AdapterInfo(data as List<DataInfo>, this@SemuaInfoActivity, 1)
                        rvSemuaInfo.adapter = adapter
                        //adapter.notifyDataSetChanged()

                        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                            androidx.appcompat.widget.SearchView.OnQueryTextListener {
                            override fun onQueryTextSubmit(p0: String?): Boolean {
                                p0?.let { adapter.filter(it) }
                                return false
                            }

                            override fun onQueryTextChange(p0: String?): Boolean {
                                p0?.let { adapter.filter(it) }
                                return false
                            }

                        })
                    }
                }else{
                    imgKosong.visibility = View.GONE
                    tvDataKosong.visibility = View.GONE
                    refreshLayout.isRefreshing = false
                    Toast.makeText(this@SemuaInfoActivity, response.message(), Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

    private fun getSemuaPengumuman(){
        NetworkConfig().api().getSemuaPengumuman().enqueue(object : Callback<Info>{
            override fun onFailure(call: Call<Info>, t: Throwable) {
                imgKosong.visibility = View.GONE
                tvDataKosong.visibility = View.GONE
                refreshLayout.isRefreshing = false
                Toast.makeText(this@SemuaInfoActivity, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Info>, response: Response<Info>) {
                refreshLayout.isRefreshing = false
                if(response.isSuccessful){
                    val data = response.body()?.data
                    if(data?.isEmpty()!!){
                        imgKosong.visibility = View.VISIBLE
                        tvDataKosong.visibility = View.VISIBLE
                    }else{
                        imgKosong.visibility = View.GONE
                        tvDataKosong.visibility = View.GONE
                        rvSemuaInfo.layoutManager = GridLayoutManager(this@SemuaInfoActivity, 2)
                        val adapter = AdapterInfo(data as List<DataInfo>, this@SemuaInfoActivity, 1)
                        rvSemuaInfo.adapter = adapter

                        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                            androidx.appcompat.widget.SearchView.OnQueryTextListener {
                            override fun onQueryTextSubmit(p0: String?): Boolean {
                                p0?.let { adapter.filter(it) }
                                return false
                            }

                            override fun onQueryTextChange(p0: String?): Boolean {
                                p0?.let { adapter.filter(it) }
                                return false
                            }

                        })
                    }
                }else{
                    imgKosong.visibility = View.GONE
                    tvDataKosong.visibility = View.GONE
                    refreshLayout.isRefreshing = false
                    Toast.makeText(this@SemuaInfoActivity, response.message(), Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

    private fun getSemuaLoker(){
        NetworkConfig().api().getSemuaLoker().enqueue(object : Callback<Info>{
            override fun onFailure(call: Call<Info>, t: Throwable) {
                imgKosong.visibility = View.GONE
                tvDataKosong.visibility = View.GONE
                refreshLayout.isRefreshing = false
                Toast.makeText(this@SemuaInfoActivity, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Info>, response: Response<Info>) {
                refreshLayout.isRefreshing = false
                if(response.isSuccessful){
                    val data = response.body()?.data
                    if(data?.isEmpty()!!){
                        imgKosong.visibility = View.VISIBLE
                        tvDataKosong.visibility = View.VISIBLE
                    }else{
                        imgKosong.visibility = View.GONE
                        tvDataKosong.visibility = View.GONE
                        rvSemuaInfo.layoutManager = GridLayoutManager(this@SemuaInfoActivity, 2)
                        val adapter =
                            AdapterInfo(data as List<DataInfo>, this@SemuaInfoActivity, 2)
                        rvSemuaInfo.adapter = adapter
                        //adapter.notifyDataSetChanged()

                        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                            androidx.appcompat.widget.SearchView.OnQueryTextListener {
                            override fun onQueryTextSubmit(p0: String?): Boolean {
                                p0?.let { adapter.filter(it) }
                                return false
                            }

                            override fun onQueryTextChange(p0: String?): Boolean {
                                p0?.let { adapter.filter(it) }
                                return false
                            }

                        })
                    }
                }else{
                    imgKosong.visibility = View.GONE
                    tvDataKosong.visibility = View.GONE
                    refreshLayout.isRefreshing = false
                    Toast.makeText(this@SemuaInfoActivity, response.message(), Toast.LENGTH_SHORT).show()
                }
            }

        })
    }
}
