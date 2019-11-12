package com.proyek2.blkindramayu.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.proyek2.blkindramayu.R
import com.proyek2.blkindramayu.viewModel.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private var context : Context? = null
    private lateinit var viewModelLogin : LoginViewModel
    private lateinit var alertDialog : Dialog
    private lateinit var loading : Dialog
    private var validUsername = false
    private var validPassword = false
    private var verifPassword = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        context = this
        alertDialog = Dialog(this)
        loading = Dialog(this)

        tvRegister.setOnClickListener {
            intent = Intent(context, RegistrasiActivity::class.java)
            startActivity(intent)
            finish()
        }

        validationRequired()

    }

    private fun validationRequired(){
        //validation
        validationUsername()
        validationPassword()
        verificationPassword()

        btnLogin.setOnClickListener {
            when {
                etUsername.text.isEmpty() -> etUsername.error = "Username Kosong"
                etPassword.text.isEmpty() -> etPassword.error = "Password Kosong"
                etVerifPassword.text.isEmpty() -> etVerifPassword.error = "Tidak Boleh Kosong"
                !validUsername -> validationUsername()
                !validPassword -> validationPassword()
                !verifPassword -> verificationPassword()
                else -> login()
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

    private fun login(){
        val username : String = etUsername.text.toString()
        val password : String = etPassword.text.toString()

        viewModelLogin = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        loading()

        Handler().postDelayed({
            viewModelLogin.setData(username, password).observe(this, Observer { t ->
//                t?.data_member?.let { storeSQLite(it as List<Member>) }
//                t?.status_code?.let { t.message?.get(0)?.let { it1 -> alertDialog(it1, it) } }
                t?.status_code?.let { t.message?.get(0)?.let { it1 -> alertDialog(it1, it, t.data_member) } }

            })
            loading.dismiss()
        }, 3000)

    }

    private fun storeSQLite(list: List<com.proyek2.blkindramayu.model.Member?>?) {
        Toast.makeText(this, list?.get(0)!!.username, Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("SetTextI18n")
    private fun alertDialog(message : String, status_code : Int, list: List<com.proyek2.blkindramayu.model.Member?>?){
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
                0 -> storeSQLite(list)
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
