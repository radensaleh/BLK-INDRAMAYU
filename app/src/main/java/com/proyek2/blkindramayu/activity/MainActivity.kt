package com.proyek2.blkindramayu.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import com.proyek2.blkindramayu.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var context : Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        context = this

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
            startActivity(intent)
            finish()
        }, 3500)
    }
}
