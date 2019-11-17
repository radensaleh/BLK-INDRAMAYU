package com.proyek2.blkindramayu.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.Toast
//import android.widget.Toast
import com.proyek2.blkindramayu.R
import com.proyek2.blkindramayu.room.AppDataBase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var context : Context? = null
    private var appDB : AppDataBase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        context = this

        appDB = AppDataBase.getInstance(this)

        val id = appDB?.memberDao()?.getMember()

        Toast.makeText(this, id.toString(), Toast.LENGTH_SHORT).show()

        animationUtils()
        loadingProgressBar()

    }

    fun animationUtils(){
        val leftRight = AnimationUtils.loadAnimation(context,
            R.anim.lefttoright
        )
        val rightLeft = AnimationUtils.loadAnimation(context,
            R.anim.righttoleft
        )
        val uptoDown  = AnimationUtils.loadAnimation(context,
            R.anim.uptodown
        )
        val downToup  = AnimationUtils.loadAnimation(context,
            R.anim.downtoup
        )



        tvSelamatDatang.startAnimation(uptoDown)
        tvUptd.startAnimation(leftRight)
        tvDinas.startAnimation(rightLeft)
        tvMenunggu.startAnimation(downToup)

        logoKemnaker.startAnimation(leftRight)
        logoPemkot.startAnimation(uptoDown)
        logoBlk.startAnimation(rightLeft)
    }

    fun loadingProgressBar(){
        Handler().postDelayed({
            intent = Intent(context, BerandaActivity::class.java)
            intent.putExtra("navTab", 1)
            startActivity(intent)
            finish()
        }, 3500)
    }
}
