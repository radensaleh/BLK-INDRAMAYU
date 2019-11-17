package com.proyek2.blkindramayu.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager

import com.proyek2.blkindramayu.R
import com.proyek2.blkindramayu.adapter.AdapterInfo
import com.proyek2.blkindramayu.adapter.PagerAdapterSlide
import com.proyek2.blkindramayu.model.DataInfo
import com.proyek2.blkindramayu.model.DataPoster
import com.proyek2.blkindramayu.viewModel.BeritaViewModel
import com.proyek2.blkindramayu.viewModel.LokerViewModel
import com.proyek2.blkindramayu.viewModel.SlidePosterViewModel
import kotlinx.android.synthetic.main.fragment_beranda.*
import java.util.*

@Suppress("UNCHECKED_CAST")
class BerandaFragment : Fragment() {

    private lateinit var viewModelBerita : BeritaViewModel
    private lateinit var viewModelLoker : LokerViewModel
    private lateinit var viewModelPoster: SlidePosterViewModel
    private var currentPage = 1
    private var numPages = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_beranda, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        shimmerPoster.startShimmerAnimation()
        shimmerInfo.startShimmerAnimation()
        shimmerLoker.startShimmerAnimation()

        slidePoster()
        berita()
        loker()
        animationUtils()

        tvSemuaBerita.setOnClickListener {
            Toast.makeText(context, "Semua Berita", Toast.LENGTH_SHORT).show()
        }

        tvSemuaLoker.setOnClickListener {
            Toast.makeText(context, "Semua Loker", Toast.LENGTH_SHORT).show()
        }

//        imgScan.setOnClickListener {
//            Toast.makeText(context, "Scan", Toast.LENGTH_SHORT).show()
//        }

    }

    private fun animationUtils(){
        val leftRight = AnimationUtils.loadAnimation(context, R.anim.lefttoright)
        val rightLeft = AnimationUtils.loadAnimation(context, R.anim.righttoleft)
        val upToDown  = AnimationUtils.loadAnimation(context, R.anim.uptodown)
        val downToup  = AnimationUtils.loadAnimation(context, R.anim.downtoup)

        imgPemkot.startAnimation(upToDown)
        imgBLK.startAnimation(upToDown)
        tvBLK.startAnimation(leftRight)
//        imgScan.startAnimation(upToDown)
        viewPager.startAnimation(downToup)
        tvBerita.startAnimation(leftRight)
        tvSemuaBerita.startAnimation(rightLeft)
        rvBerita.startAnimation(leftRight)
        tvLoker.startAnimation(leftRight)
        tvSemuaLoker.startAnimation(rightLeft)
        rvLoker.startAnimation(rightLeft)

    }


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

        val adapter = context?.let { PagerAdapterSlide(it, data) }
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
            val adapter = context?.let { AdapterInfo(data, it) }
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
            val adapter = context?.let { AdapterInfo(data, it) }
            rvLoker.adapter = adapter
            adapter?.notifyDataSetChanged()
        }

    }

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

}
