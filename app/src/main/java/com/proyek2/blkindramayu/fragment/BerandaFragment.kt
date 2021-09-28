package com.proyek2.blkindramayu.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Adapter
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager

import com.proyek2.blkindramayu.R
import com.proyek2.blkindramayu.activity.SemuaInfoActivity
import com.proyek2.blkindramayu.adapter.AdapterInfo
import com.proyek2.blkindramayu.adapter.PagerAdapterSlide
import com.proyek2.blkindramayu.model.*
import com.proyek2.blkindramayu.network.NetworkConfig
import com.proyek2.blkindramayu.room.AppDataBase
import com.proyek2.blkindramayu.room.MemberEntity
import com.proyek2.blkindramayu.viewModel.BeritaViewModel
import com.proyek2.blkindramayu.viewModel.LokerViewModel
import com.proyek2.blkindramayu.viewModel.SlidePosterViewModel
import kotlinx.android.synthetic.main.fragment_beranda.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

@Suppress("UNCHECKED_CAST")
class BerandaFragment : Fragment() {

    private lateinit var viewModelBerita : BeritaViewModel
    private lateinit var viewModelLoker : LokerViewModel
    private lateinit var viewModelPoster: SlidePosterViewModel
    private var currentPage = 1
    private var numPages = 0
    private var member : MemberEntity? = null
    private var appDB : AppDataBase? = null
    private var kdPengguna : Int? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_beranda, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        shimmerPoster.startShimmerAnimation()
        shimmerInfo.startShimmerAnimation()
        shimmerLoker.startShimmerAnimation()
        shimmerPengumuman.startShimmerAnimation()

        appDB = context?.let { AppDataBase.getInstance(it) }
        member = appDB?.memberDao()?.getMember()
        kdPengguna = appDB?.memberDao()?.getMember()?.kd_pengguna

        if(member != null){ shimmerLokerMember.startShimmerAnimation() }

        if(savedInstanceState == null){ instance() }

        refreshLayout.setOnRefreshListener { instance() }

    }

    private fun instance(){
        currentPage = 1
        getPoster()
        getBerita()
        getPengumuman()
        getLoker()

        val leftRight = AnimationUtils.loadAnimation(context, R.anim.lefttoright)
        val rightLeft = AnimationUtils.loadAnimation(context, R.anim.righttoleft)
        val upToDown  = AnimationUtils.loadAnimation(context, R.anim.uptodown)
        val downToup  = AnimationUtils.loadAnimation(context, R.anim.downtoup)

        if(member == null){
            cvLokerMember.visibility = View.GONE
        }else{
            cvLokerMember.visibility = View.VISIBLE
            tvLokerMember.startAnimation(leftRight)
            tvSemuaLokerMember.startAnimation(rightLeft)
            rvLokerMember.startAnimation(rightLeft)
            kdPengguna?.let { getMinatMember(it) }
        }

        imgPemkot.startAnimation(upToDown)
        imgBLK.startAnimation(upToDown)
        tvBLK.startAnimation(leftRight)
//        imgScan.startAnimation(upToDown)
        viewPager.startAnimation(downToup)
        tvBerita.startAnimation(leftRight)
        tvSemuaBerita.startAnimation(rightLeft)
        rvBerita.startAnimation(leftRight)

        tvPengumuman.startAnimation(leftRight)
        tvSemuaPengumuman.startAnimation(rightLeft)
        rvPengumuman.startAnimation(leftRight)

        tvLoker.startAnimation(leftRight)
        tvSemuaLoker.startAnimation(rightLeft)
        rvLoker.startAnimation(rightLeft)

        tvSemuaBerita.setOnClickListener {
            val intent = Intent(context, SemuaInfoActivity::class.java)
            intent.putExtra("info", 1)
            startActivity(intent)
        }

        tvSemuaLokerMember.setOnClickListener {
            val intent = Intent(context, SemuaInfoActivity::class.java)
            intent.putExtra("info", 2)
            startActivity(intent)
        }

        tvSemuaLoker.setOnClickListener {
            val intent = Intent(context, SemuaInfoActivity::class.java)
            intent.putExtra("info", 3)
            startActivity(intent)
        }

        tvSemuaPengumuman.setOnClickListener {
            val intent = Intent(context, SemuaInfoActivity::class.java)
            intent.putExtra("info", 4)
            startActivity(intent)
        }


    }

    //Start Tanpa Design Pattern
    @SuppressLint("WrongConstant")
    private fun getPoster(){
        NetworkConfig().api().getPoster().enqueue(object : Callback<Poster>{
            override fun onFailure(call: Call<Poster>, t: Throwable) {
                shimmerPoster.stopShimmerAnimation()
                refreshLayout.isRefreshing = false
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Poster>, response: Response<Poster>) {
                if(response.isSuccessful){
                    refreshLayout.isRefreshing = false
                    val data = response.body()?.data
                    if(data?.isEmpty()!!){
                        shimmerPoster.stopShimmerAnimation()
                        shimmerPoster.visibility = View.GONE
                        Toast.makeText(context, "Poster Tidak Tersedia!", Toast.LENGTH_SHORT).show()
                    }else{
                        shimmerPoster.stopShimmerAnimation()
                        shimmerPoster.visibility = View.GONE

                        val adapter = context?.let { PagerAdapterSlide(it, data as List<DataPoster>, 1) }
                        viewPager.adapter = adapter
                        viewPager.setPadding(50, 0, 50, 0)
                        viewPager.setCurrentItem(1, true)

                        numPages = data.size

                        val handler = Handler()
                        val update  = Runnable {
                            if(currentPage == numPages){
                                currentPage = 0
                            }
                            viewPager?.setCurrentItem(currentPage++, true)
                        }

                        val swipeTimer = Timer()
                        swipeTimer.schedule(object : TimerTask() {
                            override fun run() {
                                handler.post(update)
                            }

                        }, 3000, 3000)
                    }
                }else{
                    shimmerPoster.stopShimmerAnimation()
                    refreshLayout.isRefreshing = false
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                }
            }

        })
    }
    @SuppressLint("WrongConstant")
    private fun getBerita(){
        NetworkConfig().api().getBerita().enqueue(object : Callback<Info>{
            override fun onFailure(call: Call<Info>, t: Throwable) {
                shimmerInfo.stopShimmerAnimation()
                refreshLayout.isRefreshing = false
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Info>, response: Response<Info>) {
                if(response.isSuccessful){
                    refreshLayout.isRefreshing = false
                    val data = response.body()?.data
                    if(data?.isEmpty()!!){
                        tvBeritaKosong.visibility = View.VISIBLE
                        shimmerInfo.stopShimmerAnimation()
                        shimmerInfo.visibility = View.GONE
                    }else{
                        tvBeritaKosong.visibility = View.GONE
                        shimmerInfo.stopShimmerAnimation()
                        shimmerInfo.visibility = View.GONE

                        rvBerita.layoutManager = LinearLayoutManager(context, LinearLayout.HORIZONTAL, false)
                        val adapter = context?.let { AdapterInfo(data as List<DataInfo>, it, 1) }
                        rvBerita.adapter = adapter
                        adapter?.notifyDataSetChanged()
                    }
                }else{
                    refreshLayout.isRefreshing = false
                    shimmerInfo.stopShimmerAnimation()
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

    @SuppressLint("WrongConstant")
    private fun getPengumuman(){
        NetworkConfig().api().getPengumuman().enqueue(object : Callback<Info>{
            override fun onFailure(call: Call<Info>, t: Throwable) {
                shimmerPengumuman.stopShimmerAnimation()
                refreshLayout.isRefreshing = false
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Info>, response: Response<Info>) {
                if(response.isSuccessful){
                    refreshLayout.isRefreshing = false
                    val data = response.body()?.data
                    if(data?.isEmpty()!!){
                        tvPengumumanKosong.visibility = View.VISIBLE
                        shimmerPengumuman.stopShimmerAnimation()
                        shimmerPengumuman.visibility = View.GONE
                    }else{
                        tvPengumumanKosong.visibility = View.GONE
                        shimmerPengumuman.stopShimmerAnimation()
                        shimmerPengumuman.visibility = View.GONE

                        rvPengumuman.layoutManager = LinearLayoutManager(context, LinearLayout.HORIZONTAL, false)
                        val adapter = context?.let { AdapterInfo(data as List<DataInfo>, it, 1) }
                        rvPengumuman.adapter = adapter
                        adapter?.notifyDataSetChanged()
                    }
                }else{
                    refreshLayout.isRefreshing = false
                    shimmerPengumuman.stopShimmerAnimation()
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

    @SuppressLint("WrongConstant")
    private fun getLoker(){
        NetworkConfig().api().getLoker().enqueue(object : Callback<Info>{
            override fun onFailure(call: Call<Info>, t: Throwable) {
                shimmerLoker.stopShimmerAnimation()
                refreshLayout.isRefreshing = false
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Info>, response: Response<Info>) {
                if(response.isSuccessful){
                    refreshLayout.isRefreshing = false
                    val data = response.body()?.data
                    if(data?.isEmpty()!!){
                        tvLokerKosong.visibility = View.VISIBLE
                        shimmerLoker.stopShimmerAnimation()
                        shimmerLoker.visibility = View.GONE
                    }else{
                        tvLokerKosong.visibility = View.GONE
                        shimmerLoker.stopShimmerAnimation()
                        shimmerLoker.visibility = View.GONE

                        rvLoker.layoutManager = LinearLayoutManager(context, LinearLayout.HORIZONTAL, false)
                        val adapter = context?.let { AdapterInfo(data as List<DataInfo>, it, 2) }
                        rvLoker.adapter = adapter
                        adapter?.notifyDataSetChanged()
                    }
                }else{
                    shimmerLoker.stopShimmerAnimation()
                    refreshLayout.isRefreshing = false
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                }
            }

        })
    }
    private fun getMinatMember(kdPengguna : Int){
        NetworkConfig().api().getMinatByMember(kdPengguna).enqueue(object : Callback<Minat>{
            override fun onFailure(call: Call<Minat>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Minat>, response: Response<Minat>) {
                if (response.isSuccessful){
                    val data = response.body()?.data
                    val listMinat = ArrayList<Int>()

                    for(i in data!!.indices){ data[i]?.id_minat?.let { listMinat.add(it) } }
                    lokerByMinat(listMinat)
                }else{
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                }
            }

        })
    }
    @SuppressLint("WrongConstant")
    private fun lokerByMinat(listMinat : ArrayList<Int>){
        NetworkConfig().api().getLokerByMinat(listMinat).enqueue(object : Callback<Info>{
            override fun onFailure(call: Call<Info>, t: Throwable) {
                refreshLayout.isRefreshing = false
                shimmerLokerMember.stopShimmerAnimation()
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Info>, response: Response<Info>) {
                if(response.isSuccessful){
                    refreshLayout.isRefreshing = false
                    if(response.body()?.data?.isEmpty()!!){
                        tvLokerMemberKosong.visibility = View.VISIBLE
                        shimmerLokerMember.stopShimmerAnimation()
                        shimmerLokerMember.visibility = View.GONE
                    }else{
                        tvLokerMemberKosong.visibility = View.GONE
                        shimmerLokerMember.stopShimmerAnimation()
                        shimmerLokerMember.visibility = View.GONE

                        val data = response.body()!!.data

                        rvLokerMember.layoutManager = LinearLayoutManager(context, LinearLayout.HORIZONTAL, false)
                        val adapter = context?.let { AdapterInfo(data as List<DataInfo>, it, 2) }
                        rvLokerMember.adapter = adapter
                        adapter?.notifyDataSetChanged()
                    }
                }else{
                    refreshLayout.isRefreshing = false
                    shimmerLokerMember.stopShimmerAnimation()
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                }
            }

        })
    }
    //End Tanpa Design Pattern

    //Start MVVM
    private fun slidePoster(){
        viewModelPoster = ViewModelProviders.of(this).get(SlidePosterViewModel::class.java)
        viewModelPoster.getStatus().observe(this, Observer {
                t ->
            if(t == true){
                viewPager.visibility = View.GONE
            }else{
                viewPager.visibility = View.VISIBLE
            }
        })

        viewModelPoster.setData().observe(this, Observer {
                t ->
            t?.data?.let { showPoster(it as List<DataPoster>) }
        })

        refreshLayout.isRefreshing = false
    }
    private fun berita(){
        viewModelBerita = ViewModelProviders.of(this).get(BeritaViewModel::class.java)
        viewModelBerita.getStatus().observe(this, Observer {
                t ->
            if (t == true) {
                rvBerita.visibility = View.GONE
            }else {
                rvBerita.visibility = View.VISIBLE
            }
        })

        viewModelBerita.setData().observe(this, Observer {
                t ->
            t?.data?.let { showBerita(it as List<DataInfo>) }
        })

        refreshLayout.isRefreshing = false
    }
    private fun loker(){
        viewModelLoker = ViewModelProviders.of(this).get(LokerViewModel::class.java)
        viewModelLoker.getStatus().observe(this, Observer {
                t ->
            if (t == true)
                rvLoker.visibility = View.GONE
            else
                rvLoker.visibility = View.VISIBLE
        })
        viewModelLoker.setData().observe(this, Observer {
                t ->
            t?.data?.let { showLoker(it as List<DataInfo>) }
        })

        refreshLayout.isRefreshing = false
    }
    private fun showPoster(data : List<DataPoster>){
        shimmerPoster.stopShimmerAnimation()
        shimmerPoster.visibility = View.GONE

        val adapter = context?.let { PagerAdapterSlide(it, data, 1) }
        viewPager.adapter = adapter
        viewPager.setPadding(50, 0, 50, 0)
        viewPager.setCurrentItem(1, true)

        numPages = data.size

        val handler = Handler()
        val update  = Runnable {
            if(currentPage == numPages){
                currentPage = 0
            }
            viewPager?.setCurrentItem(currentPage++, true)
        }

        val swipeTimer = Timer()
        swipeTimer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(update)
            }

        }, 3000, 3000)
        //viewPager.setPageTransformer(true, ZoomOutPageTransformer())
    }
    @SuppressLint("WrongConstant")
    private fun showBerita(data: List<DataInfo>){
        if(data.isEmpty()){
            tvBeritaKosong.visibility = View.VISIBLE
            shimmerInfo.stopShimmerAnimation()
            shimmerInfo.visibility = View.GONE

        }else{
            tvBeritaKosong.visibility = View.GONE
            shimmerInfo.stopShimmerAnimation()
            shimmerInfo.visibility = View.GONE

            rvBerita.layoutManager = LinearLayoutManager(context, LinearLayout.HORIZONTAL, false)
            val adapter = context?.let { AdapterInfo(data, it, 1) }
            rvBerita.adapter = adapter
            adapter?.notifyDataSetChanged()
        }

    }
    @SuppressLint("WrongConstant")
    private fun showLoker(data: List<DataInfo>){
        if(data.isEmpty()){
            tvLokerKosong.visibility = View.VISIBLE
            shimmerLoker.stopShimmerAnimation()
            shimmerLoker.visibility = View.GONE

        }else{
            tvLokerKosong.visibility = View.GONE
            shimmerLoker.stopShimmerAnimation()
            shimmerLoker.visibility = View.GONE

            rvLoker.layoutManager = LinearLayoutManager(context, LinearLayout.HORIZONTAL, false)
            val adapter = context?.let { AdapterInfo(data, it, 2) }
            rvLoker.adapter = adapter
            adapter?.notifyDataSetChanged()
        }

    }
    //End MVVM

    override fun onPause() {
        shimmerPoster.stopShimmerAnimation()
        shimmerInfo.stopShimmerAnimation()
        shimmerLoker.stopShimmerAnimation()
        super.onPause()
    }
    override fun onResume() {
        super.onResume()
        shimmerPoster.startShimmerAnimation()
        shimmerInfo.startShimmerAnimation()
        shimmerLoker.startShimmerAnimation()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }
}
