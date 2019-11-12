package com.proyek2.blkindramayu.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import com.proyek2.blkindramayu.R
import kotlinx.android.synthetic.main.activity_registrasi.*
import java.util.*

class RegistrasiActivity : AppCompatActivity() {

    private var context : Context? = null
    private var jk = ""
    private var tglLahir = ""
    private var validUsername = false
    private var validPassword = false
    private var verifPassword = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrasi)

        context = this

        tvLogin.setOnClickListener {
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        validationRequired()
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

        //validation
        validationUsername()
        validationPassword()
        verificationPassword()

        btnDaftar.setOnClickListener {
            when {
                etNIK.text.isEmpty() -> etNIK.error = "NIK Kosong"
                etNama.text.isEmpty() -> etNama.error = "Nama Kosong"
                tglLahir.isEmpty() -> Toast.makeText(this, "Pilih Tanggal Lahir!", Toast.LENGTH_SHORT).show()
                selected == -1 -> Toast.makeText(this, "Pilih Jenis Kelamin!", Toast.LENGTH_SHORT).show()
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
        Toast.makeText(this, jk, Toast.LENGTH_SHORT).show()
        Toast.makeText(this, tglLahir, Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("SetTextI18n")
    private fun date(){
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        btnTglLahir.setOnClickListener {
            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                tglLahir = "$dayOfMonth / $month / $year"
                tvTglLahir.text = tglLahir
            }, year, month, day)
            dpd.show()
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
}
