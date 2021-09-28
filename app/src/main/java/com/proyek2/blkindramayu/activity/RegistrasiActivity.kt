package com.proyek2.blkindramayu.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.*
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.proyek2.blkindramayu.R
import com.proyek2.blkindramayu.adapter.AdapterMinat
import com.proyek2.blkindramayu.model.DataMinat
import com.proyek2.blkindramayu.model.Kota
import com.proyek2.blkindramayu.model.Minat
import com.proyek2.blkindramayu.model.Provinsi
import com.proyek2.blkindramayu.network.NetworkConfig
import com.proyek2.blkindramayu.viewModel.RegistrasiViewModel
import kotlinx.android.synthetic.main.activity_registrasi.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

@Suppress("UNCHECKED_CAST")
class RegistrasiActivity : AppCompatActivity() {

    private var context : Context? = null
    private var jk = ""
    private var tglLahir = ""
    private var validNIK = false
    private var validUsername = false
    private var validPassword = false
    private var verifPassword = false
    private var tmptLahir : Int? = null
    private var idKota : Int? = null
    private var idProvinsi : Int? = null
    private var idMinat = ArrayList<Int>()
    private lateinit var viewModelRegister : RegistrasiViewModel
    private lateinit var loading : Dialog
    private lateinit var alertDialog : Dialog
    private lateinit var dialogMinat : Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrasi)

        context = this
        loading = Dialog(this)
        alertDialog = Dialog(this)
        dialogMinat = Dialog(this)

        tvLogin.setOnClickListener {
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        getKota()
        getProvinsi()

        validationRequired()
    }

    private fun getKota(){
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
                        applicationContext, R.layout.support_simple_spinner_dropdown_item,
                        listSpinner
                    )

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spTempatLahir.setAdapter(adapter)

                    spTempatLahir.setOnItemSelectedListener { view, position, id, item ->
                        tmptLahir = data[position].id_kota
                    }

//                    spTempatLahir.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
//                        override fun onNothingSelected(p0: AdapterView<*>?) {
//                            //nothing
//                        }
//
//                        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//                            tmptLahir = data[p2].id_kota
//                            //Toast.makeText(context, tmptLahir, Toast.LENGTH_SHORT).show()
//                        }
//
//                    }

                }else{
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                }
            }

        })

    }

    private fun getProvinsi(){
        NetworkConfig().api().getProvinsi().enqueue(object : Callback<List<Provinsi>> {
            override fun onFailure(call: Call<List<Provinsi>>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<List<Provinsi>>, response: Response<List<Provinsi>>) {
                if(response.isSuccessful){
                    val data : List<Provinsi> = response.body()!!

                    val listSpinner = ArrayList<String>()
                    for(i in data.indices){
                        listSpinner.add(data[i].provinsi!!)
                    }

                    val adapter = ArrayAdapter(
                        applicationContext, R.layout.support_simple_spinner_dropdown_item,
                        listSpinner
                    )

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spProvinsi.setAdapter(adapter)

                    val listKota : List<Kota> = data[0].listkota!!

                    val listSpinnerKota = ArrayList<String>()
                    for(i in listKota.indices){
                        listSpinnerKota.add(listKota[i].type!!+ " " + listKota[i].kota!!)
                    }

                    val adapterKota = ArrayAdapter(
                        applicationContext, R.layout.support_simple_spinner_dropdown_item,
                        listSpinnerKota
                    )

                    adapterKota.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spKota.setAdapter(adapterKota)

                    spProvinsi.setOnItemSelectedListener { view, position, id, item ->
                        idProvinsi = data[position].id_provinsi
                        //Toast.makeText(context, idProvinsi.toString(), Toast.LENGTH_SHORT).show()

                        val listKota : List<Kota> = data[position].listkota!!

                        val listSpinnerKota = ArrayList<String>()
                        for(i in listKota.indices){
                            listSpinnerKota.add(listKota[i].type!!+ " " + listKota[i].kota!!)
                        }

                        val adapterKota = ArrayAdapter(
                            applicationContext, R.layout.support_simple_spinner_dropdown_item,
                            listSpinnerKota
                        )

                        adapterKota.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spKota.setAdapter(adapterKota)

                        spKota.setOnItemSelectedListener { view, position, id, item ->
                            idKota = listKota[position].id_kota
                            //Toast.makeText(context, idKota.toString(), Toast.LENGTH_SHORT).show()
                        }

                    }

//                    spProvinsi.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
//                        override fun onNothingSelected(p0: AdapterView<*>?) {
//                            //nothing
//                        }
//
//                        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//                            idProvinsi = data[p2].id_provinsi
//                            //Toast.makeText(context, idProvinsi, Toast.LENGTH_SHORT).show()
//
//                            val listKota : List<Kota> = data[p2].listkota!!
//
//                            val listSpinnerKota = ArrayList<String>()
//                            for(i in listKota.indices){
//                                listSpinnerKota.add(listKota[i].type!!+ " " + listKota[i].kota!!)
//                            }
//
//                            val adapterKota = ArrayAdapter(
//                                applicationContext, R.layout.support_simple_spinner_dropdown_item,
//                                listSpinnerKota
//                            )
//
//                            adapterKota.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//                            spKota.adapter = adapterKota
//
//                            spKota.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
//                                override fun onNothingSelected(p0: AdapterView<*>?) {
//                                    //nothing
//                                }
//
//                                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//                                    idKota = listKota[p2].id_kota
//                                    //Toast.makeText(context, idKota, Toast.LENGTH_SHORT).show()
//                                }
//
//                            }
//
//                        }
//
//                    }

                }else{
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

    private fun validationRequired(){
        //radio group
        var selected : Int = rbGroupJk.checkedRadioButtonId

        rbGroupJk.setOnCheckedChangeListener { _, i ->
            selected = i
            val radioButton : RadioButton? = findViewById(selected)
            jk = radioButton?.text.toString()
        }

        //date
        date()
        //minat
        getMinat()

        //validation
        validationNIK()
        validationUsername()
        validationPassword()
        verificationPassword()

        btnDaftar.setOnClickListener {
            when {
                etNIK.text.isEmpty() -> etNIK.error = "NIK Kosong!"
                !validNIK -> validationNIK()
                etNama.text.isEmpty() -> etNama.error = "Nama Kosong"
                tmptLahir == null -> Toast.makeText(this, "Tempat Lahir Kosong!", Toast.LENGTH_SHORT).show()
                tglLahir.isEmpty() -> Toast.makeText(this, "Pilih Tanggal Lahir!", Toast.LENGTH_SHORT).show()
                selected == -1 -> Toast.makeText(this, "Pilih Jenis Kelamin!", Toast.LENGTH_SHORT).show()
                tvMinat.text.toString() == "Minat : []" -> Toast.makeText(context, "Pilih Minat Anda!", Toast.LENGTH_SHORT).show()
                idProvinsi == null -> Toast.makeText(this, "Provinsi Kosong!", Toast.LENGTH_SHORT).show()
                idKota == null -> Toast.makeText(this, "Kota/Kabupaten Kosong!", Toast.LENGTH_SHORT).show()
                etAlamatLengkap.text.isEmpty() -> etAlamatLengkap.error = "Alamat Kosong"
                etKodePos.text.isEmpty() -> etKodePos.error = "Kode Pos Kosong"
                etUsername.text.isEmpty() -> etUsername.error = "Username Kosong"
                etPassword.text.isEmpty() -> etPassword.error = "Password Kosong"
                etVerifPassword.text.isEmpty() -> etVerifPassword.error = "Tidak Boleh Kosong"
                etEmail.text.isEmpty() -> etEmail.error = "Email Kosong"
                !validUsername -> validationUsername()
                !validPassword -> validationPassword()
                !verifPassword -> verificationPassword()
                else -> daftar()
            }
        }
    }

    private fun daftar(){
        val nik = etNIK.text.toString()
        val nama = etNama.text.toString()
        val alamat = etAlamatLengkap.text.toString()
        val kodepos = etKodePos.text.toString()
        val username = etUsername.text.toString()
        val password = etPassword.text.toString()
        val email = etEmail.text.toString()

        loading()

        Handler().postDelayed({
            viewModelRegister = ViewModelProviders.of(this).get(RegistrasiViewModel::class.java)
            viewModelRegister.setData(nik,nama, tmptLahir!!.toInt(), tglLahir, jk, idProvinsi!!.toInt(), idKota!!.toInt(), alamat, kodepos.toInt(), username, password, email, 0, idMinat).observe(this, androidx.lifecycle.Observer {
                    t ->
//                Toast.makeText(this, t?.message?.get(0), Toast.LENGTH_SHORT).show()
                t?.status_code?.let { alertDialog(t.message?.get(0).toString(), it) }
            })

            viewModelRegister.getResponse().observe(this, androidx.lifecycle.Observer {
                    t ->
                Toast.makeText(this, t, Toast.LENGTH_SHORT).show()
            })

            loading.dismiss()
        }, 3000)

//        Toast.makeText(context, nik, Toast.LENGTH_SHORT).show()
//        Toast.makeText(context, nama, Toast.LENGTH_SHORT).show()
//        Toast.makeText(context, tmptLahir, Toast.LENGTH_SHORT).show()
//        Toast.makeText(context, tglLahir, Toast.LENGTH_SHORT).show()
//        Toast.makeText(context, jk, Toast.LENGTH_SHORT).show()
//        Toast.makeText(context, idProvinsi, Toast.LENGTH_SHORT).show()
//        Toast.makeText(context, idKota, Toast.LENGTH_SHORT).show()
//        Toast.makeText(context, alamat, Toast.LENGTH_SHORT).show()
//        Toast.makeText(context, username, Toast.LENGTH_SHORT).show()
//        Toast.makeText(context, password, Toast.LENGTH_SHORT).show()
//        Toast.makeText(context, email, Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("SetTextI18n")
    private fun date(){
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        btnTglLahir.setOnClickListener {
            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                tglLahir = "$dayOfMonth/$month/$year"
                tvTglLahir.text = tglLahir
            }, year, month, day)
            dpd.show()
        }
    }

    private fun getMinat(){
        NetworkConfig().api().getMinat().enqueue(object : Callback<Minat>{
            override fun onFailure(call: Call<Minat>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Minat>, response: Response<Minat>) {
                if(response.isSuccessful){
                    val listMinat = response.body()?.data
                    minat(listMinat as List<DataMinat>?)
                }else{
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

    @SuppressLint("SetTextI18n", "WrongConstant")
    private fun minat(data : List<DataMinat>?){
        val minatPilihan : List<DataMinat>? = null

        btnMinat.setOnClickListener {
            dialogMinat.setContentView(R.layout.dialog_minat)
            val judul = dialogMinat.findViewById<TextView>(R.id.tvJudul)
            val rvMinat = dialogMinat.findViewById<RecyclerView>(R.id.rvMinat)
            val btnOK = dialogMinat.findViewById<Button>(R.id.btnOK)

            judul.text = "Pilih Minat Anda"

            rvMinat.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
            val adapter = context?.let { it1 ->
                data?.let { it2 ->
                    AdapterMinat(
                        it2,
                        it1, dialogMinat, btnOK, tvMinat, minatPilihan
                    )
                }
            }

            adapter?.dataID = object : AdapterMinat.InterfaceID{
                override fun getID(pilihanID: ArrayList<Int>) {
                    idMinat = pilihanID
                }
            }

            rvMinat.adapter = adapter
            adapter?.notifyDataSetChanged()

            dialogMinat.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialogMinat.window!!.attributes.windowAnimations = R.style.DialogAnimation
            dialogMinat.setCancelable(false)
            dialogMinat.show()
        }
    }

    private fun validationNIK(){
        etNIK.doAfterTextChanged {
            if(etNIK.text.toString().length < 16 || etNIK.text.toString().length > 16){
                etNIK.error = "NIK Harus 16 Karakter"
                validNIK = false
            }else{
                validNIK = true
            }
        }
    }

    private fun validationUsername(){
        etUsername.doAfterTextChanged {
            if(etUsername.text.toString().length < 4) {
                etUsername.error = "Username Minimal 4 Karakter"
                validUsername = false
            }else{
                validUsername = true
            }
        }
    }

    private fun validationPassword(){
        etPassword.doAfterTextChanged {
            if(etPassword.text.toString().length < 6) {
                etPassword.error = "Password Minimal 6 Karakter"
                validPassword = false
            }else{
                if(etVerifPassword.text.toString() != etPassword.text.toString()){
                    etPassword.error = "Password Tidak Sama"
                    validPassword = false
                }else{
                    validPassword = true
                    verifPassword = true
                    etVerifPassword.error = null
                }
            }
        }

    }

    private fun verificationPassword(){
        etVerifPassword.doAfterTextChanged {
            if(etVerifPassword.text.toString() != etPassword.text.toString()){
                etVerifPassword.error = "Password Tidak Sama"
                verifPassword = false
            }else{
                verifPassword = true
                validPassword = true
                etPassword.error = null
            }
        }
    }

    private fun loading(){
        loading.setContentView(R.layout.loading)
        loading.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        loading.window!!.attributes.windowAnimations = R.style.DialogAnimation
        loading.setCancelable(false)
        loading.show()
    }

    @SuppressLint("SetTextI18n")
    private fun alertDialog(message : String, status_code : Int){
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
                    val intent = Intent(context, BerandaActivity::class.java)
                    intent.putExtra("navTab", 4)
                    startActivity(intent)
                    finish()
                }
            }
        }

        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        alertDialog.setCancelable(false)
        alertDialog.show()

    }
}
