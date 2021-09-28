package com.proyek2.blkindramayu.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager

import com.proyek2.blkindramayu.R
import com.proyek2.blkindramayu.activity.HistoriPelatihanActivity
import com.proyek2.blkindramayu.adapter.AdapterPelatihan
import com.proyek2.blkindramayu.model.Pelatihan
import com.proyek2.blkindramayu.network.NetworkConfig
import com.proyek2.blkindramayu.room.AppDataBase
import com.proyek2.blkindramayu.room.MemberEntity
import kotlinx.android.synthetic.main.fragment_pelatihan.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PelatihanFragment : Fragment() {
    private var member : MemberEntity? = null
    private var appDB : AppDataBase? = null
    private lateinit var loading : Dialog
    private lateinit var dialogDaftar : Dialog
    private lateinit var alertDialog : Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_pelatihan, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        appDB = context?.let { AppDataBase.getInstance(it) }
        member = appDB?.memberDao()?.getMember()
        loading = Dialog(context!!)
        dialogDaftar = Dialog(context!!)
        alertDialog = Dialog(context!!)

        if(savedInstanceState == null){
            loading()
            hideComponent()
            Handler().postDelayed({
                getPelatihan()
            }, 1000)
        }

    }

    private fun hideComponent(){
        tvHeader.visibility = View.GONE
        tv1.visibility = View.GONE
        tv2.visibility = View.GONE
        tv3.visibility = View.GONE
        tvKosong.visibility = View.GONE

        if(member == null){
            btnRiwayat.visibility = View.GONE
        }else{
            btnRiwayat.visibility = View.GONE
        }
    }

    fun showComponent(){
        tvHeader.visibility = View.VISIBLE
        tv1.visibility = View.VISIBLE
        tv2.visibility = View.VISIBLE
        tv3.visibility = View.VISIBLE

        if(member == null){
            btnRiwayat.visibility = View.GONE
        }else{
            btnRiwayat.visibility = View.VISIBLE
        }

        animationUtils()
    }

    private fun animationUtils(){
        val leftRight = AnimationUtils.loadAnimation(context, R.anim.lefttoright)
        val rightLeft = AnimationUtils.loadAnimation(context, R.anim.righttoleft)
        val upToDown  = AnimationUtils.loadAnimation(context, R.anim.uptodown)
        //val downToup  = AnimationUtils.loadAnimation(context, R.anim.downtoup)

        if(member == null){
            btnRiwayat.visibility = View.GONE
        }else{
            btnRiwayat.startAnimation(rightLeft)
            btnRiwayat.setOnClickListener {
                val i = Intent(context, HistoriPelatihanActivity::class.java)
                startActivity(i)
            }
        }

        tvHeader.startAnimation(upToDown)
        tv1.startAnimation(leftRight)
        tv2.startAnimation(leftRight)
        tv3.startAnimation(leftRight)
        tvGelombang.startAnimation(leftRight)
        rvGelombang.startAnimation(leftRight)
    }

    private fun getPelatihan(){
        NetworkConfig().api().getPelatihan().enqueue(object : Callback<Pelatihan>{
            override fun onFailure(call: Call<Pelatihan>, t: Throwable) {
                loading.dismiss()
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                showComponent()
            }

            @SuppressLint("WrongConstant")
            override fun onResponse(call: Call<Pelatihan>, response: Response<Pelatihan>) {
                loading.dismiss()
                showComponent()
                if(response.isSuccessful){
                    val data = response.body()
                    if(data?.data?.size == 0){
                        tvKosong.visibility = View.VISIBLE
                    }else{
                        tvKosong.visibility = View.GONE

                        rvGelombang.layoutManager = LinearLayoutManager(context, LinearLayout.HORIZONTAL, false)
                        val adapter =
                            data?.data?.let { context?.let { it1 ->
                                AdapterPelatihan(it,
                                    it1, dialogDaftar, member, loading, alertDialog)
                            } }

                        rvGelombang.adapter = adapter
                        adapter?.notifyDataSetChanged()

                    }

                }else{
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

    private fun loading(){
        loading.setContentView(R.layout.loading)
        loading.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        loading.window!!.attributes.windowAnimations = R.style.DialogAnimation
        loading.setCancelable(false)
        loading.show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //Toast.makeText(context, "Saved Instance", Toast.LENGTH_SHORT).show()
    }
}
