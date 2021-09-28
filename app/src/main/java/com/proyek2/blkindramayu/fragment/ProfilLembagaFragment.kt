package com.proyek2.blkindramayu.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.proyek2.blkindramayu.R
import com.proyek2.blkindramayu.adapter.PagerAdapterProfilLembaga
import com.proyek2.blkindramayu.model.DataProfilLembaga
import com.proyek2.blkindramayu.viewModel.ProfilLembagaViewModel
import kotlinx.android.synthetic.main.fragment_profil_lembaga.*
import java.util.*

@Suppress("UNCHECKED_CAST")
class ProfilLembagaFragment : Fragment() {

    private lateinit var viewModelProfilLembagaViewModel: ProfilLembagaViewModel
    private var currentPage = 0
    private var numPages = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profil_lembaga, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        shimmerProfileLembaga.startShimmerAnimation()

        animationUtils()
        slideData()

    }

    private fun animationUtils(){
        val leftRight = AnimationUtils.loadAnimation(context, R.anim.lefttoright)
        val upToDown  = AnimationUtils.loadAnimation(context, R.anim.uptodown)
        val downToup  = AnimationUtils.loadAnimation(context, R.anim.downtoup)

        tvProfilLembaga.startAnimation(upToDown)
        viewPager.startAnimation(downToup)
        tvEmail.startAnimation(leftRight)
        tvKontak.startAnimation(leftRight)
        tvAlamat.startAnimation(leftRight)
    }

    @SuppressLint("SetTextI18n")
    fun slideData(){
        viewModelProfilLembagaViewModel = ViewModelProviders.of(this).get(ProfilLembagaViewModel::class.java)
        viewModelProfilLembagaViewModel.getStatus().observe(this, Observer {
            t ->
            if(t == true){
                viewPager.visibility = View.GONE
            }else{
                viewPager.visibility = View.VISIBLE
            }
        })

        viewModelProfilLembagaViewModel.setData().observe(this, Observer {
            t ->
            t?.data?.let { showData(it as List<DataProfilLembaga>) }
            if(t?.alamat !=  null && t.email != null && t.kontak != null && t.id != null){
                tvEmail.text = t.email
                tvKontak.text = t.kontak
                tvAlamat.text = t.alamat
            }else{
                tvEmail.text = " menunggu...."
                tvKontak.text = " menunggu...."
                tvAlamat.text = " menungggu...."
            }

        })
    }

    private fun showData(data : List<DataProfilLembaga>){
        shimmerProfileLembaga.stopShimmerAnimation()
        shimmerProfileLembaga.visibility = View.GONE

        val adapter = context?.let { PagerAdapterProfilLembaga(data, it) }
        viewPager.adapter = adapter
        viewPager.setPageTransformer(true, ZoomOutPageTransformer())

        numPages = data.size

        val handler = Handler()
        val update = Runnable {
            if(currentPage == numPages){
                currentPage = 0
            }
            viewPager?.setCurrentItem(currentPage++, true)
        }

        val swipTimer = Timer()
        swipTimer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(update)
            }

        }, 2500, 2500)
    }

//    override fun onPause() {
//        shimmerProfileLembaga.stopShimmerAnimation()
//        super.onPause()
//    }
//
//    override fun onResume() {
//        super.onResume()
//        shimmerProfileLembaga.stopShimmerAnimation()
//    }
}
