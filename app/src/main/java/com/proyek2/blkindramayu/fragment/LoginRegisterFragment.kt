package com.proyek2.blkindramayu.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.proyek2.blkindramayu.R
import com.proyek2.blkindramayu.activity.LoginActivity
import com.proyek2.blkindramayu.activity.RegistrasiActivity
import kotlinx.android.synthetic.main.fragment_login_register.*

class LoginRegisterFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login_register, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        cvLogin.setOnClickListener {
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
        }

        cvRegister.setOnClickListener {
            val intent = Intent(context, RegistrasiActivity::class.java)
            startActivity(intent)
        }

    }
}
