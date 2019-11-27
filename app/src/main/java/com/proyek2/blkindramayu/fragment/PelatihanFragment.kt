package com.proyek2.blkindramayu.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils

import com.proyek2.blkindramayu.R
import com.proyek2.blkindramayu.room.AppDataBase
import com.proyek2.blkindramayu.room.MemberEntity
import kotlinx.android.synthetic.main.fragment_pelatihan.*

class PelatihanFragment : Fragment() {
    private var member : MemberEntity? = null
    private var appDB : AppDataBase? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_pelatihan, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        appDB = context?.let { AppDataBase.getInstance(it) }
        member = appDB?.memberDao()?.getMember()

        animationUtils()

    }

    fun animationUtils(){
        val leftRight = AnimationUtils.loadAnimation(context, R.anim.lefttoright)
        val rightLeft = AnimationUtils.loadAnimation(context, R.anim.righttoleft)
        val upToDown  = AnimationUtils.loadAnimation(context, R.anim.uptodown)
        val downToup  = AnimationUtils.loadAnimation(context, R.anim.downtoup)

        if(member == null){
            btnRiwayat.visibility = View.GONE
        }else{
            btnRiwayat.startAnimation(rightLeft)
        }

        tvHeader.startAnimation(upToDown)
        tv1.startAnimation(leftRight)
        tv2.startAnimation(leftRight)
        tv3.startAnimation(leftRight)
        tvGelombang.startAnimation(leftRight)

    }
}
