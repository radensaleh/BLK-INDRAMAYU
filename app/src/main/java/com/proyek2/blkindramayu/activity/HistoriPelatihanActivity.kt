package com.proyek2.blkindramayu.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.proyek2.blkindramayu.R
import com.proyek2.blkindramayu.adapter.AdapterPelatihanByMember
import com.proyek2.blkindramayu.model.Pelatihan
import com.proyek2.blkindramayu.network.NetworkConfig
import com.proyek2.blkindramayu.room.AppDataBase
import com.proyek2.blkindramayu.room.MemberEntity
import kotlinx.android.synthetic.main.activity_histori_pelatihan.*
import retrofit2.Call
import retrofit2.Response

class HistoriPelatihanActivity : AppCompatActivity() {

    private var member : MemberEntity? = null
    private var appDB : AppDataBase? = null
    private lateinit var loading : Dialog
    private lateinit var dialogSertifikat : Dialog
    private lateinit var context : Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_histori_pelatihan)

        loading = Dialog(this)
        dialogSertifikat = Dialog(this)
        appDB = AppDataBase.getInstance(this)
        member = appDB?.memberDao()?.getMember()
        context = this

        if(savedInstanceState == null){
            permission()
            loading()
            hideComponent()
            Handler().postDelayed({
                getPelatihanByMember()
            }, 1000)
        }

    }

    private fun permission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1000)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            1000 -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(context, "Izin Diberikan!", Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(context, "Izin Ditolak!", Toast.LENGTH_LONG).show()
                }
            }
        }

    }

    fun hideComponent(){
        imgKosong.visibility = View.GONE
        tvDataKosong.visibility = View.GONE
        LLHeader.visibility = View.GONE
    }

    fun showComponent(){
        imgKosong.visibility = View.GONE
        tvDataKosong.visibility = View.GONE
        LLHeader.visibility = View.VISIBLE

        animationUtils()
    }

    fun animationUtils(){
        val leftRight = AnimationUtils.loadAnimation(context, R.anim.lefttoright)
        val rightLeft = AnimationUtils.loadAnimation(context, R.anim.righttoleft)
        val upToDown  = AnimationUtils.loadAnimation(context, R.anim.uptodown)
        val downToup  = AnimationUtils.loadAnimation(context, R.anim.downtoup)

        imgHistori.startAnimation(upToDown)
        tvHistori.startAnimation(leftRight)
        tvHistori2.startAnimation(leftRight)
        imgKosong.startAnimation(downToup)
        tvDataKosong.startAnimation(downToup)
        rvPelatihanByMember.startAnimation(rightLeft)
    }

    fun getPelatihanByMember(){
        NetworkConfig().api().getPelatihanByMember(member?.kd_pengguna!!).enqueue(object : retrofit2.Callback<Pelatihan>{
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
                        tvDataKosong.visibility = View.VISIBLE
                        imgKosong.visibility = View.VISIBLE
                    }else{
                        tvDataKosong.visibility = View.GONE
                        imgKosong.visibility = View.GONE

                        rvPelatihanByMember.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
                        val adapter = AdapterPelatihanByMember(data?.data!!, context, dialogSertifikat)
                        rvPelatihanByMember.adapter = adapter
                        adapter.notifyDataSetChanged()
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
