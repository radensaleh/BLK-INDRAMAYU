package com.proyek2.blkindramayu.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.proyek2.blkindramayu.R
import com.proyek2.blkindramayu.activity.LoginActivity
import com.proyek2.blkindramayu.network.NetworkConfig
import com.proyek2.blkindramayu.room.AppDataBase
import com.proyek2.blkindramayu.room.MemberEntity
import com.proyek2.blkindramayu.weather.Result
import kotlinx.android.synthetic.main.fragment_akun.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AkunFragment : Fragment() {

    private var appDB : AppDataBase? = null
    private var member : MemberEntity? = null
    private lateinit var alertDialog : Dialog
    private lateinit var loading : Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_akun, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        appDB = context?.let { AppDataBase.getInstance(it) }
        member = appDB?.memberDao()?.getMember()
        alertDialog = context?.let { Dialog(it) }!!
        loading = Dialog(context!!)

        loading()
        hideComponent()

        setData(member)

        updateDataDiri()
        updateUkuran()
        updateAlamat()

        btnLogout.setOnClickListener {
            alert()
        }

    }

    private fun hideComponent(){
        imgUcapan.visibility = View.GONE
        imgWeather.visibility = View.GONE
        tvC.visibility = View.GONE
        tvUcapan.visibility = View.GONE
        tvNamaLengkap.visibility = View.GONE
        tvKTP.visibility = View.GONE
        tvEmail.visibility = View.GONE
        cvDataDiri.visibility = View.GONE
        cvUkuran.visibility = View.GONE
        btnLogout.visibility = View.GONE
        editDataDiri.visibility = View.GONE
        editUkuran.visibility = View.GONE
        editAlamat.visibility = View.GONE
        cvAlamat.visibility = View.GONE
    }

    private fun showComponent(){
        imgUcapan.visibility = View.VISIBLE
        imgWeather.visibility = View.VISIBLE
        tvC.visibility = View.VISIBLE
        tvUcapan.visibility = View.VISIBLE
        tvNamaLengkap.visibility = View.VISIBLE
        tvKTP.visibility = View.VISIBLE
        tvEmail.visibility = View.VISIBLE
        cvDataDiri.visibility = View.VISIBLE
        cvUkuran.visibility = View.VISIBLE
        btnLogout.visibility = View.VISIBLE
        editDataDiri.visibility = View.VISIBLE
        editUkuran.visibility = View.VISIBLE
        editAlamat.visibility = View.VISIBLE
        cvAlamat.visibility = View.VISIBLE
    }

    private fun animationUtils(){
        val leftRight = AnimationUtils.loadAnimation(context, R.anim.lefttoright)
        val rightLeft = AnimationUtils.loadAnimation(context, R.anim.righttoleft)
        val upToDown  = AnimationUtils.loadAnimation(context, R.anim.uptodown)
        val downToup  = AnimationUtils.loadAnimation(context, R.anim.downtoup)

        imgUcapan.startAnimation(upToDown)
        imgWeather.startAnimation(rightLeft)
        tvC.startAnimation(rightLeft)
        tvUcapan.startAnimation(upToDown)
        tvNamaLengkap.startAnimation(leftRight)
        tvKTP.startAnimation(leftRight)
        tvEmail.startAnimation(leftRight)
        cvDataDiri.startAnimation(leftRight)
        cvUkuran.startAnimation(rightLeft)
        btnLogout.startAnimation(rightLeft)
        editDataDiri.startAnimation(leftRight)
        editUkuran.startAnimation(leftRight)
        editAlamat.startAnimation(leftRight)
        cvAlamat.startAnimation(downToup)
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun setData(member: MemberEntity?){
        if (member != null) {
            tvNamaLengkap.text = member.nama_lengkap
            tvKTP.text = " NIK. ${member.nomor_ktp}"
            tvEmail.text = " ${member.email}"
            tvTTL.text = " ${member.tempat_lahir}, ${member.tgl_lahir}"
            tvJK.text = " ${member.jenis_kelamin}"
            tvKontak.text  = " ${member.nomor_kontak}"

            when{ member.jenis_kelamin == "Perempuan" -> imgJK.setImageResource(R.drawable.jkperempuan)
                else -> { imgJK.setImageResource(R.drawable.jklakilaki) }  }

            when{ member.nomor_kontak == null -> tvKontak.text = " - "
                else -> { tvKontak.text = " ${member.nomor_kontak}" } }

            when{ member.pend_terakhir == null -> tvPend.text = " - "
                else -> { tvPend.text = " Pendidikan ${member.pend_terakhir}" } }

            when{ member.thn_ijazah == null -> tvThnLulus.text = " - "
                else -> { tvThnLulus.text = " Lulus ${member.thn_ijazah}" } }

            when{ member.ukuran_baju == null -> tvUkuranBaju.text = " - "
                else -> { tvUkuranBaju.text = " ${member.ukuran_baju}" } }

            when{ member.ukuran_sepatu == null -> tvUkuranSepatu.text = " - "
                else -> { tvUkuranSepatu.text = " ${member.ukuran_sepatu}" } }

            tvProvinsi.text = " ${member.kabupaten_kota}, ${member.provinsi}"
            tvKodePos.text = " Kode Pos. ${member.kodepos}"
            tvAlamatLengkap.text = " ${member.alamat_lengkap}"

            getCurrentWeather(member.kabupaten_kota)

        }else{
            loading.dismiss()
            showComponent()
            animationUtils()
            Toast.makeText(context, "Data Kosong!", Toast.LENGTH_SHORT).show()
        }
    }


    private fun logout(member : MemberEntity?){
        member?.let { appDB?.memberDao()?.delete(it) }
        val intent = Intent(context, LoginActivity::class.java)
        startActivity(intent)
    }

    @SuppressLint("SetTextI18n")
    private fun alert(){
        alertDialog.setContentView(R.layout.alert_logout)

        val btnYa : Button = alertDialog.findViewById(R.id.btnYa)
        val btnNo : Button = alertDialog.findViewById(R.id.btnTidak)
        val tvIsi : TextView = alertDialog.findViewById(R.id.tvIsi)
        val tvJudul : TextView = alertDialog.findViewById(R.id.tvJudul)

        tvJudul.text = "Peringatan!"
        tvIsi.text = "Anda yakin ingin keluar akun ?"
        btnYa.setOnClickListener {
            alertDialog.dismiss()
            logout(member)

        }

        btnNo.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun updateDataDiri(){
        editDataDiri.setOnClickListener {

        }
    }

    private fun updateUkuran(){
        editUkuran.setOnClickListener {

        }
    }

    private fun updateAlamat(){
        editAlamat.setOnClickListener {

        }
    }

    private fun getCurrentWeather(city : String){
        NetworkConfig().api().getCurrentWeather(city).enqueue(object : Callback<Result>{
            override fun onFailure(call: Call<Result>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                loading.dismiss()
                showComponent()
                animationUtils()
            }

            override fun onResponse(call: Call<Result>, response: Response<Result>) {
                if(response.isSuccessful){
                    loading.dismiss()
                    showComponent()
                    animationUtils()

                    val listData  = response.body()
                    if (listData != null) {
                        context?.let {
                            Glide.with(it)
                                .load("https://www.weatherbit.io/static/img/icons/${listData.data!![0]?.weather!!.icon}.png")
                                .into(imgWeather)
                        }

                        tvC.text = listData.data?.get(0)?.temp?.toInt().toString() + "°C"
                    }


                }else{
                    loading.dismiss()
                    showComponent()
                    animationUtils()

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

}
