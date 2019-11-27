package com.proyek2.blkindramayu.fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
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
import android.widget.*
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.proyek2.blkindramayu.R
import com.proyek2.blkindramayu.activity.BerandaActivity
import com.proyek2.blkindramayu.activity.LoginActivity
import com.proyek2.blkindramayu.adapter.AdapterMinat
import com.proyek2.blkindramayu.model.*
import com.proyek2.blkindramayu.network.NetworkConfig
import com.proyek2.blkindramayu.room.AppDataBase
import com.proyek2.blkindramayu.room.MemberEntity
import com.proyek2.blkindramayu.weather.Result
import kotlinx.android.synthetic.main.fragment_akun.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

@Suppress("NAME_SHADOWING", "UNCHECKED_CAST")
class AkunFragment : Fragment() {

    private var appDB : AppDataBase? = null
    private var member : MemberEntity? = null
    private lateinit var alertDialog : Dialog
    private lateinit var loading : Dialog
    private lateinit var dialogDataDiri : Dialog
    private lateinit var dialogUkuran : Dialog
    private lateinit var dialogAlamat : Dialog
    private lateinit var dialogPassword : Dialog
    private lateinit var dialogMinat : Dialog
    private var selectedJK : Int? = null
    private var jk = ""
    private var tglLahir = ""
    private var tmptLahir : Int? = null
    private var idProvinsi : Int? = null
    private var pendidikan = ""
    private var ukuranSepatu : Int? = null
    private var ukuranBaju = ""
    private var selectedBaju : Int? = null
    private var validThnLulus = false
    private var validPassword = false
    private var validPasswordBaru = false
    private var validVerifPassword = false
    private var dataMinat : List<DataMinat>? = null

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
        dialogDataDiri = Dialog(context!!)
        dialogUkuran = Dialog(context!!)
        dialogAlamat = Dialog(context!!)
        dialogPassword = Dialog(context!!)
        dialogMinat  = Dialog(context!!)

        loading()
        hideComponent()

        setData(member)

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
        cvEditPassword.visibility = View.GONE
        cvEditMinat.visibility = View.GONE
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
        cvEditPassword.visibility = View.VISIBLE
        cvEditMinat.visibility = View.VISIBLE
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
        cvEditPassword.startAnimation(leftRight)
        cvEditMinat.startAnimation(rightLeft)
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun setData(member: MemberEntity?){
        val kdPengguna = member?.kd_pengguna
        val username = member?.username

        getMinat()

        NetworkConfig().api().getMember(kdPengguna!!, username!!).enqueue(object : Callback<Res>{
            override fun onFailure(call: Call<Res>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Res>, response: Response<Res>) {
                val member = response.body()?.data_member
                val minat = response.body()?.minat_member

                if(response.isSuccessful){
                    if (member != null) {
                        tvNamaLengkap.text = member[0]!!.nama_lengkap
                        tvKTP.text = " NIK. ${member[0]!!.nomor_ktp}"
                        tvEmail.text = " ${member[0]!!.email}"
                        tvTTL.text = " ${member[0]!!.tempat_lahir}, ${member[0]!!.tgl_lahir}"
                        tvJK.text = " ${member[0]!!.jenis_kelamin}"
                        tvKontak.text  = " ${member[0]!!.nomor_kontak}"

                        when{ member[0]!!.jenis_kelamin == "Perempuan" -> imgJK.setImageResource(R.drawable.jkperempuan)
                            else -> { imgJK.setImageResource(R.drawable.jklakilaki) }  }

                        when{ member[0]!!.nomor_kontak == null -> tvKontak.text = " - "
                            else -> { tvKontak.text = " ${member[0]!!.nomor_kontak}" } }

                        when{ member[0]!!.pend_terakhir == null -> tvPend.text = " - "
                            else -> { tvPend.text = " Pendidikan ${member[0]!!.pend_terakhir}" } }

                        when{ member[0]!!.thn_ijazah == null -> tvThnLulus.text = " - "
                            else -> { tvThnLulus.text = " Lulus ${member[0]!!.thn_ijazah}" } }

                        when{ member[0]!!.ukuran_baju == null -> tvUkuranBaju.text = " - "
                            else -> { tvUkuranBaju.text = " ${member[0]!!.ukuran_baju}" } }

                        when{ member[0]!!.ukuran_sepatu == null -> tvUkuranSepatu.text = " - "
                            else -> { tvUkuranSepatu.text = " ${member[0]!!.ukuran_sepatu}" } }

                        tvProvinsi.text = " ${member[0]!!.kabupaten_kota}, ${member[0]!!.provinsi}"
                        tvKodePos.text = " Kode Pos. ${member[0]!!.kodepos}"
                        tvAlamatLengkap.text = " ${member[0]!!.alamat_lengkap}"

                        getCurrentWeather(member[0]?.kabupaten_kota!!)
                        updateDataDiri(member)
                        updateUkuran(member)
                        updateAlamat(member)
                        updatePassword(member)
                        updateMinat(member, dataMinat, minat)

                    }else{
                        loading.dismiss()
                        showComponent()
                        animationUtils()
                        Toast.makeText(context, "Data Kosong!", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                }
            }

        })

    }


    private fun logout(member : MemberEntity?){
        NetworkConfig().api().updateToken(member?.username!!, "", 1).enqueue(object : Callback<Res>{
            override fun onFailure(call: Call<Res>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Res>, response: Response<Res>) {
                if(response.isSuccessful){
                    member.let { appDB?.memberDao()?.delete(it) }

                    val intent = Intent(context, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)

                }else{
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                }
            }

        })

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

    @SuppressLint("SetTextI18n", "ResourceType")
    private fun updateDataDiri(member: List<Member?>?){
        editDataDiri.setOnClickListener {
            dialogDataDiri.setContentView(R.layout.update_datadiri)
            val tvJudul = dialogDataDiri.findViewById<TextView>(R.id.tvJudul)
            val etNIK = dialogDataDiri.findViewById<EditText>(R.id.etNIK)
            val etUsername = dialogDataDiri.findViewById<EditText>(R.id.etUsername)
            val etNamaLengkap = dialogDataDiri.findViewById<EditText>(R.id.etNama)
            val spTmptLahir = dialogDataDiri.findViewById<Spinner>(R.id.spTempatLahir)
            val btnKalender = dialogDataDiri.findViewById<Button>(R.id.btnTglLahir)
            val tvTglLahir = dialogDataDiri.findViewById<TextView>(R.id.tvTglLahir)
            val rgJK = dialogDataDiri.findViewById<RadioGroup>(R.id.rbGroupJk)
            val rbL = dialogDataDiri.findViewById<RadioButton>(R.id.rbL)
            val rbP = dialogDataDiri.findViewById<RadioButton>(R.id.rbP)
            val spPendTerakhir = dialogDataDiri.findViewById<Spinner>(R.id.spPendidikan)
            val etThnLulus = dialogDataDiri.findViewById<EditText>(R.id.etThnLulus)
            val etNoKontak = dialogDataDiri.findViewById<EditText>(R.id.etNoTelp)
            val etEmail = dialogDataDiri.findViewById<EditText>(R.id.etEmail)
            val btnClose = dialogDataDiri.findViewById<Button>(R.id.btnClose)
            val btnUpdate = dialogDataDiri.findViewById<Button>(R.id.btnUpdate)

            date(btnKalender, tvTglLahir)
            getKota(spTmptLahir, member?.get(0)?.id_kotalahir!!)

            if(member[0]?.pend_terakhir != null){ pendidikanTerakhir(spPendTerakhir, member[0]?.pend_terakhir) }else{ pendidikanTerakhir(spPendTerakhir, member[0]?.pend_terakhir) }
            if(member[0]?.thn_ijazah != null){ etThnLulus.setText(member[0]?.thn_ijazah.toString()) }
            if(member[0]?.nomor_kontak != null){ etNoKontak.setText(member[0]?.nomor_kontak) }

            etNIK.setText("NIK. ${member[0]?.nomor_ktp}")
            etNIK.isEnabled = false
            etUsername.setText("Username. ${member[0]?.username}")
            etUsername.isEnabled = false
            etNamaLengkap.setText(member[0]?.nama_lengkap)
            etEmail.setText(member[0]?.email)

            tvTglLahir.text = member[0]?.tgl_lahir
            tglLahir = member[0]?.tgl_lahir!!
            jk = member[0]?.jenis_kelamin!!

            if(jk == "Perempuan"){ rbP.isChecked = true }else{ rbL.isChecked = true }

            rgJK.setOnCheckedChangeListener { _, i ->
                selectedJK = i
                val radioButton = dialogDataDiri.findViewById<RadioButton>(selectedJK!!)
                jk = radioButton?.text.toString()
            }
            validThn(etThnLulus)
            tvJudul.text = "Ubah Data Diri"
            btnClose.setOnClickListener { dialogDataDiri.dismiss() }
            btnUpdate.setOnClickListener {
                when{
                    etNamaLengkap.text.isEmpty() -> etNamaLengkap.error = "Nama Kosong!"
                    etThnLulus.text.isEmpty() -> etThnLulus.error = "Tahun Lulus Kosong!"
                    !validThnLulus -> validThn(etThnLulus)
                    etNoKontak.text.isEmpty() -> etNoKontak.error = "Nomor Kontak Kosong!"
                    etEmail.text.isEmpty() -> etEmail.error = "Email Kosong!"
                    else -> {
                        val nik = member[0]?.nomor_ktp
                        val username = member[0]?.username
                        val nama = etNamaLengkap.text.toString()
                        val tglLahir = tglLahir
                        val jk = jk
                        val thnLulus = etThnLulus.text.toString()
                        val noKontak = etNoKontak.text.toString()
                        val email = etEmail.text.toString()


                        loading()
                        Handler().postDelayed({
                            prosesUpdateDataDiri(nik!!, username!!, nama, tmptLahir!!, tglLahir, jk, pendidikan, thnLulus.toInt(), noKontak, email, dialogDataDiri)
                        }, 2000)
                    }
                }

//                Toast.makeText(context, nik, Toast.LENGTH_SHORT).show()
//                Toast.makeText(context, username, Toast.LENGTH_SHORT).show()
//                Toast.makeText(context, nama, Toast.LENGTH_SHORT).show()
//                Toast.makeText(context, tmptLahir, Toast.LENGTH_SHORT).show()
//                Toast.makeText(context, tglLahir, Toast.LENGTH_SHORT).show()
//                Toast.makeText(context, jk, Toast.LENGTH_SHORT).show()
//                Toast.makeText(context, pendidikan, Toast.LENGTH_SHORT).show()
//                Toast.makeText(context, thnLulus, Toast.LENGTH_SHORT).show()
//                Toast.makeText(context, noKontak, Toast.LENGTH_SHORT).show()
//                Toast.makeText(context, email, Toast.LENGTH_SHORT).show()
            }

            dialogDataDiri.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialogDataDiri.window!!.attributes.windowAnimations = R.style.DialogAnimation
            dialogDataDiri.setCancelable(false)
            dialogDataDiri.show()
        }
    }

    private fun validThn(etThnLulus : EditText){
        etThnLulus.doAfterTextChanged {
            if(etThnLulus.text.toString().length < 4 || etThnLulus.text.toString().length > 4) {
                etThnLulus.error = "Tahun Lulus Harus 4 Karakter"
                validThnLulus = false
            }else{
                validThnLulus = true
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun date(btnTglLahir : Button, tvTglLahir : TextView){
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        btnTglLahir.setOnClickListener {
            val dpd = DatePickerDialog(context!!, DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                tglLahir = "$dayOfMonth/$month/$year"
                tvTglLahir.text = tglLahir
            }, year, month, day)
            dpd.show()
        }
    }

    private fun getKota(spTempatLahir : Spinner, id : Int){
        NetworkConfig().api().getKota().enqueue(object : Callback<List<Kota>>{
            override fun onFailure(call: Call<List<Kota>>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<List<Kota>>, response: Response<List<Kota>>) {
                if(response.isSuccessful){
                    val data : List<Kota> = response.body()!!

                    val listSpinner = ArrayList<String>()
                    for (i in data.indices) {
                        listSpinner.add(data[i].type!!+ " " + data[i].kota!!)
                    }

                    val adapter = ArrayAdapter(
                        context?.applicationContext!!, R.layout.support_simple_spinner_dropdown_item,
                        listSpinner
                    )

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spTempatLahir.adapter = adapter
                    spTempatLahir.setSelection(id-1)

                    spTempatLahir.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                        override fun onNothingSelected(p0: AdapterView<*>?) {
                            //nothing
                        }

                        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                            tmptLahir = data[p2].id_kota
                            //Toast.makeText(context, tmptLahir, Toast.LENGTH_SHORT).show()
                        }

                    }


                }else{
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                }
            }

        })

    }

    private fun pendidikanTerakhir(spPendTerakhir : Spinner, pend : String? = null){
        val listPendidikan = ArrayList<String>()
        listPendidikan.add("SD/MI")
        listPendidikan.add("SMP/MTs")
        listPendidikan.add("SLTA")
        listPendidikan.add("D3")
        listPendidikan.add("S1/D4")
        listPendidikan.add("S2")

        val adapter = ArrayAdapter(
            context?.applicationContext!!, R.layout.support_simple_spinner_dropdown_item,
            listPendidikan
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spPendTerakhir.adapter = adapter

        for(i in listPendidikan.indices){
            if(listPendidikan[i] == pend){
                spPendTerakhir.setSelection(i)
            }
        }

        spPendTerakhir.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
                //nothing
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                pendidikan = listPendidikan[p2]
                //Toast.makeText(context, pendidikan, Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun prosesUpdateDataDiri(nik : String, username : String, nama : String, tmptLahir : Int, tglLahir : String, jk : String, pend : String, thnLulus : Int, noKontak : String, email : String, dialog: Dialog){
        NetworkConfig().api().updateDataDiri(nik, username, nama, tmptLahir, tglLahir, jk, pend, thnLulus, noKontak, email).enqueue(object : Callback<Res>{
            override fun onFailure(call: Call<Res>, t: Throwable) {
                loading.dismiss()
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Res>, response: Response<Res>) {
                loading.dismiss()

                val res = response.body()
                if(response.isSuccessful){
                    alertDialog(res?.message!![0]!!, res.status_code!!, dialog)
                }else{
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

    @SuppressLint("SetTextI18n")
    private fun updateUkuran(member: List<Member?>?){
        editUkuran.setOnClickListener {
            dialogUkuran.setContentView(R.layout.update_ukuran)
            val spUkuranSepatu = dialogUkuran.findViewById<Spinner>(R.id.spUkuranSepatu)
            val rgUkuranBaju = dialogUkuran.findViewById<RadioGroup>(R.id.rbGroupBaju)
            val tvJudul = dialogUkuran.findViewById<TextView>(R.id.tvJudul)
            val s = dialogUkuran.findViewById<RadioButton>(R.id.S)
            val m = dialogUkuran.findViewById<RadioButton>(R.id.M)
            val l = dialogUkuran.findViewById<RadioButton>(R.id.L)
            val xl = dialogUkuran.findViewById<RadioButton>(R.id.XL)
            val xxl = dialogUkuran.findViewById<RadioButton>(R.id.XXL)
            val xxxl = dialogUkuran.findViewById<RadioButton>(R.id.XXXL)
            val btnUpdate = dialogUkuran.findViewById<Button>(R.id.btnUpdate)
            val btnClose = dialogUkuran.findViewById<Button>(R.id.btnClose)
            val username = member?.get(0)?.username

            tvJudul.text = "Ubah Data Ukuran"
            ukuranSepatu(spUkuranSepatu, member?.get(0)!!.ukuran_sepatu)

            if(member[0]?.ukuran_baju != null){ ukuranBaju = member[0]?.ukuran_baju!! }

            when(ukuranBaju){
                "S" -> s.isChecked = true
                "M" -> m.isChecked = true
                "L" -> l.isChecked = true
                "XL" -> xl.isChecked = true
                "2XL" -> xxl.isChecked = true
                "3XL" -> xxxl.isChecked = true
            }

            rgUkuranBaju.setOnCheckedChangeListener { _, i ->
                selectedBaju = i
                val radioButton = dialogUkuran.findViewById<RadioButton>(selectedBaju!!)
                ukuranBaju = radioButton?.text.toString()
            }

            btnUpdate.setOnClickListener {
                loading()
                Handler().postDelayed({
                    prosesUpdateUkuran(username!!, ukuranBaju, ukuranSepatu!!, dialogUkuran)
                }, 2000)
            }

            btnClose.setOnClickListener {
                dialogUkuran.dismiss()
            }

            dialogUkuran.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialogUkuran.window!!.attributes.windowAnimations = R.style.DialogAnimation
            dialogUkuran.setCancelable(false)
            dialogUkuran.show()
        }
    }
    private fun ukuranSepatu(spUkuranSepatu : Spinner, ukuran : Int? = null){
        val listUkuran = ArrayList<Int>()
        for(i in 30..50){
            listUkuran.add(i)
        }

        val adapter = ArrayAdapter(
            context?.applicationContext!!, R.layout.support_simple_spinner_dropdown_item,
            listUkuran
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spUkuranSepatu.adapter = adapter

        for(i in listUkuran.indices){
            if(listUkuran[i] == ukuran){
                spUkuranSepatu.setSelection(i)
            }
        }

        spUkuranSepatu.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
                //nothing
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                ukuranSepatu = listUkuran[p2]
                //Toast.makeText(context, ukuranSepatu.toString(), Toast.LENGTH_SHORT).show()
            }

        }

    }
    private fun prosesUpdateUkuran(username : String, baju : String, sepatu : Int, dialog: Dialog){
        NetworkConfig().api().updateUkuran(username, baju, sepatu).enqueue(object : Callback<Res>{
            override fun onFailure(call: Call<Res>, t: Throwable) {
                loading.dismiss()
                Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<Res>, response: Response<Res>) {
                loading.dismiss()

                val res = response.body()
                if(response.isSuccessful){
                    alertDialog(res?.message!![0]!!, res.status_code!!, dialog)
                }else{
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

    @SuppressLint("SetTextI18n")
    private fun updateAlamat(member: List<Member?>?){
        editAlamat.setOnClickListener {
            dialogAlamat.setContentView(R.layout.update_alamat)
            val tvJudul = dialogAlamat.findViewById<TextView>(R.id.tvJudul)
            val spProvinsi = dialogAlamat.findViewById<Spinner>(R.id.spProvinsi)
            val spKota = dialogAlamat.findViewById<Spinner>(R.id.spKota)
            val etAlamat = dialogAlamat.findViewById<EditText>(R.id.etAlamatLengkap)
            val etKodepos = dialogAlamat.findViewById<EditText>(R.id.etKodePos)
            val btnUpdate = dialogAlamat.findViewById<Button>(R.id.btnUpdate)
            val btnClose = dialogAlamat.findViewById<Button>(R.id.btnClose)
            val username = member?.get(0)?.username

            tvJudul.text = "Ubah Data Alamat"
            getProvinsi(spProvinsi, spKota, member?.get(0)?.id_provinsi!!, member[0]?.id_kota!!)

            etAlamat.setText(member[0]?.alamat_lengkap)
            etKodepos.setText(member[0]?.kodepos.toString())

            btnClose.setOnClickListener { dialogAlamat.dismiss() }
            btnUpdate.setOnClickListener {
                when{
                    etAlamat.text.isEmpty() -> etAlamat.error = "Alamat Kosong!"
                    etKodepos.text.isEmpty() -> etKodepos.error = "Kodepos Kosong!"
                    else -> {
                        val alamatLengkap = etAlamat.text.toString()
                        val kodepos = etKodepos.text.toString()
                        loading()

                        Handler().postDelayed({
                            prosesUpdateAlamat(username!!, alamatLengkap, idProvinsi!!, tmptLahir!!, kodepos.toInt(), dialogAlamat)
                        }, 2000)
                    }
                }
            }

            dialogAlamat.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialogAlamat.window!!.attributes.windowAnimations = R.style.DialogAnimation
            dialogAlamat.setCancelable(false)
            dialogAlamat.show()
        }
    }

    private fun getProvinsi(spProvinsi : Spinner, spKota : Spinner, id : Int, idkota : Int){
        NetworkConfig().api().getProvinsi().enqueue(object : Callback<List<Provinsi>>{
            override fun onFailure(call: Call<List<Provinsi>>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<List<Provinsi>>, response: Response<List<Provinsi>>) {
                if(response.isSuccessful){
                    val data : List<Provinsi> = response.body()!!

                    val listSpinner = ArrayList<String>()
                    for (i in data.indices) {
                        listSpinner.add(data[i].provinsi!!)
                    }

                    val adapter = ArrayAdapter(
                        context?.applicationContext!!, R.layout.support_simple_spinner_dropdown_item,
                        listSpinner
                    )

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spProvinsi.adapter = adapter
                    spProvinsi.setSelection(id-1)

                    spProvinsi.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                        override fun onNothingSelected(p0: AdapterView<*>?) {
                            //nothing
                        }

                        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                            idProvinsi = data[p2].id_provinsi
                            //Toast.makeText(context, tmptLahir, Toast.LENGTH_SHORT).show()

                            val listKota : List<Kota> = data[p2].listkota!!

                            val listSpinnerKota = ArrayList<String>()
                            for(i in listKota.indices){
                                listSpinnerKota.add(listKota[i].type!!+ " " + listKota[i].kota!!)
                            }

                            val adapterKota = ArrayAdapter(
                                context!!.applicationContext, R.layout.support_simple_spinner_dropdown_item,
                                listSpinnerKota
                            )

                            adapterKota.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            spKota.adapter = adapterKota

                            for(i in listKota.indices){
                                if(listKota[i].id_kota == idkota){
                                    spKota.setSelection(i)
                                }
                            }

                            spKota.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                                override fun onNothingSelected(p0: AdapterView<*>?) {
                                    //nothing
                                }

                                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                                    tmptLahir = listKota[p2].id_kota
                                    //Toast.makeText(context, tmptLahir.toString(), Toast.LENGTH_SHORT).show()
                                }

                            }
                        }

                    }
                }else{
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

    private fun prosesUpdateAlamat(username: String, alamat : String, provinsi : Int, kota : Int, kodepos : Int, dialog: Dialog){
        NetworkConfig().api().updateAlamat(username, alamat, provinsi, kota, kodepos).enqueue(object : Callback<Res>{
            override fun onFailure(call: Call<Res>, t: Throwable) {
                loading.dismiss()
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Res>, response: Response<Res>) {
                loading.dismiss()

                val res = response.body()
                if(response.isSuccessful){
                    alertDialog(res?.message!![0]!!, res.status_code!!, dialog)
                }else{
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

    @SuppressLint("SetTextI18n")
    private fun updatePassword(member: List<Member?>?){
        cvEditPassword.setOnClickListener {
            dialogPassword.setContentView(R.layout.update_password)
            val judul = dialogPassword.findViewById<TextView>(R.id.tvJudul)
            val etPassword = dialogPassword.findViewById<EditText>(R.id.etPassword)
            val etPasswordBaru = dialogPassword.findViewById<EditText>(R.id.etPasswordBaru)
            val etVerifPasswordBaru = dialogPassword.findViewById<EditText>(R.id.etVerifPasswordBaru)
            val btnUpdate = dialogPassword.findViewById<Button>(R.id.btnUpdate)
            val btnClose = dialogPassword.findViewById<Button>(R.id.btnClose)

            judul.text = "Ubah Password"

            validationPasswordLama(etPassword)
            validationPasswordBaru(etPasswordBaru, etVerifPasswordBaru)
            verificationPassword(etVerifPasswordBaru, etPasswordBaru)

            btnClose.setOnClickListener { dialogPassword.dismiss() }
            btnUpdate.setOnClickListener {
                when{
                    etPassword.text.isEmpty() -> etPassword.error = "Password Kosong!"
                    etPasswordBaru.text.isEmpty() -> etPasswordBaru.error = "Password Baru Kosong!"
                    etVerifPasswordBaru.text.isEmpty() -> etVerifPasswordBaru.error = "Verifikasi Password Kosong!"
                    !validPassword -> validationPasswordLama(etPassword)
                    !validPasswordBaru -> validationPasswordBaru(etPasswordBaru, etVerifPasswordBaru)
                    !validVerifPassword -> verificationPassword(etVerifPasswordBaru, etPasswordBaru)
                    else -> {
                        val passwordLama = etPassword.text.toString()
                        val passwordBaru = etPasswordBaru.text.toString()

                        loading()
                        Handler().postDelayed({
                            prosesUpdatePassword(member, passwordLama, passwordBaru, dialogPassword)
                        }, 2000)

                    }
                }
            }

            dialogPassword.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialogPassword.window!!.attributes.windowAnimations = R.style.DialogAnimation
            dialogPassword.setCancelable(false)
            dialogPassword.show()

        }
    }

    private fun validationPasswordLama(etPassword: EditText){
        etPassword.doAfterTextChanged {
            if(etPassword.text.toString().length < 6) {
                etPassword.error = "Password Minimal 6 Karakter"
                validPassword = false
            }else{
                validPassword = true
            }
        }
    }

    private fun validationPasswordBaru(etPasswordBaru : EditText, etVerifPassword : EditText){
        etPasswordBaru.doAfterTextChanged {
            if(etPasswordBaru.text.toString().length < 6) {
                etPasswordBaru.error = "Password Baru Minimal 6 Karakter"
                validPasswordBaru = false
            }else{
                if(etVerifPassword.text.toString() != etPasswordBaru.text.toString()){
                    etPasswordBaru.error = "Password Baru Tidak Sama"
                    validPasswordBaru = false
                }else{
                    validPasswordBaru = true
                    validVerifPassword = true
                    etVerifPassword.error = null
                }
            }
        }

    }

    private fun verificationPassword(etVerifPassword : EditText, etPasswordBaru : EditText){
        etVerifPassword.doAfterTextChanged {
            if(etVerifPassword.text.toString() != etPasswordBaru.text.toString()){
                etVerifPassword.error = "Password Baru Tidak Sama"
                validVerifPassword = false
            }else{
                validVerifPassword = true
                validPasswordBaru = true
                etPasswordBaru.error = null
            }
        }
    }

    private fun prosesUpdatePassword(member: List<Member?>?, passwordLama : String, passwordBaru : String, dialog : Dialog){
        NetworkConfig().api().updatePassword(member?.get(0)?.username!!, passwordLama, passwordBaru).enqueue(object : Callback<Res>{
            override fun onFailure(call: Call<Res>, t: Throwable) {
                loading.dismiss()
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Res>, response: Response<Res>) {
                loading.dismiss()

                val res = response.body()
                if(response.isSuccessful){
                    alertDialog(res?.message!![0]!!, res.status_code!!, dialog)
                }else{
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

    private fun getMinat(){
        NetworkConfig().api().getMinat().enqueue(object : Callback<Minat>{
            override fun onFailure(call: Call<Minat>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Minat>, response: Response<Minat>) {
                if(response.isSuccessful){
                    val listMinat = response.body()?.data
                    dataMinat = listMinat as List<DataMinat>?
                }else{
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

    @SuppressLint("SetTextI18n", "WrongConstant")
    private fun updateMinat(member: List<Member?>?, minat : List<DataMinat>?, minatPilihan : List<DataMinat>?){
        cvEditMinat.setOnClickListener {
            dialogMinat.setContentView(R.layout.update_minat)
            val judul = dialogMinat.findViewById<TextView>(R.id.tvJudul)
            val rvMinat = dialogMinat.findViewById<RecyclerView>(R.id.rvMinat)
            val btnUpdate = dialogMinat.findViewById<Button>(R.id.btnUpdate)
            val btnClose = dialogMinat.findViewById<Button>(R.id.btnClose)
            var idMinat = ArrayList<Int>()

            judul.text = "Ubah Minat"

            rvMinat.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
            val adapter = context?.let { it1 ->
                minat?.let { it2 ->
                    val tvMinat : TextView? = null
                    val adapterMinat = AdapterMinat(it2, it1, dialogMinat, btnUpdate, tvMinat,
                        minatPilihan
                    )
                    adapterMinat
                }
            }

            adapter?.dataID = object : AdapterMinat.InterfaceID{
                override fun getID(pilihanID: ArrayList<Int>) {
                    idMinat = pilihanID
                }
            }

            rvMinat.adapter = adapter
            adapter?.notifyDataSetChanged()

            btnClose.setOnClickListener { dialogMinat.dismiss() }
            btnUpdate.setOnClickListener {
                if(idMinat.isEmpty()){
                    Toast.makeText(context, "Anda Belum Memilih Minat!", Toast.LENGTH_SHORT).show()
                }else{
                    loading()
                    Handler().postDelayed({
                        prosesUpdateMinat(member?.get(0)?.kd_pengguna!!, idMinat, dialogMinat)
                    }, 2000)
                }
            }

            dialogMinat.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialogMinat.window!!.attributes.windowAnimations = R.style.DialogAnimation
            dialogMinat.setCancelable(false)
            dialogMinat.show()

        }
    }

    private fun prosesUpdateMinat(kdPengguna : Int, idMinat : ArrayList<Int>, dialog: Dialog){
        NetworkConfig().api().updateMinat(kdPengguna, idMinat).enqueue(object : Callback<Res>{
            override fun onFailure(call: Call<Res>, t: Throwable) {
                loading.dismiss()
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Res>, response: Response<Res>) {
                loading.dismiss()

                val res = response.body()
                if(response.isSuccessful){
                    alertDialog(res?.message!![0]!!, res.status_code!!, dialog)
                }else{
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                }
            }

        })
    }


    @SuppressLint("SetTextI18n")
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

    @SuppressLint("SetTextI18n")
    private fun alertDialog(message : String, status_code : Int, dialog: Dialog){
        when (status_code) {
            0 -> alertDialog.setContentView(R.layout.alert_success)
            else -> alertDialog.setContentView(R.layout.alert_danger)
        }

        val btnYa : Button = alertDialog.findViewById(R.id.btnYa)
        val tvIsi : TextView = alertDialog.findViewById(R.id.tvIsi)
        val tvJudul : TextView = alertDialog.findViewById(R.id.tvJudul)

        tvJudul.text = "Peringatan!"
        tvIsi.text = message
        btnYa.setOnClickListener {
            alertDialog.dismiss()
            when (status_code) {
                0 -> {
                    dialog.dismiss()

                    val intent = Intent(context, BerandaActivity::class.java)
                    intent.putExtra("navTab", 4)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
            }
        }

        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        alertDialog.setCancelable(false)
        alertDialog.show()

    }


    private fun loading(){
        loading.setContentView(R.layout.loading)
        loading.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        loading.window!!.attributes.windowAnimations = R.style.DialogAnimation
        loading.setCancelable(false)
        loading.show()
    }

}
