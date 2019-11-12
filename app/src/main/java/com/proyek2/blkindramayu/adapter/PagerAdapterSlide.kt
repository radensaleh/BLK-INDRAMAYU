package com.proyek2.blkindramayu.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.proyek2.blkindramayu.BuildConfig
import com.proyek2.blkindramayu.R
import com.proyek2.blkindramayu.model.DataPoster

class PagerAdapterSlide(val context : Context, val posterList : List<DataPoster>) : PagerAdapter(){

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(container.context).inflate(R.layout.item_slide, container, false)

        val poster : DataPoster = posterList[position]

        if(poster.id == "EMPTY"){

            val tvJudulPoster = view.findViewById<TextView>(R.id.tvJudulPoster)
            val imgPoster     = view.findViewById<ImageView>(R.id.imgPoster)
            val cardView      = view.findViewById<CardView>(R.id.cardView)

            tvJudulPoster.visibility = View.GONE
            cardView.visibility = View.GONE

            Glide.with(context)
                .load(BuildConfig.IMAGE + "/konten/" + poster.poster)
                .into(imgPoster)

            container.addView(view, 0)

        }else{

            val tvJudulPoster = view.findViewById<TextView>(R.id.tvJudulPoster)
            val imgPoster     = view.findViewById<ImageView>(R.id.imgPoster)

            tvJudulPoster.text = poster.judul

            Glide.with(context)
                .load(BuildConfig.IMAGE + "/konten/" + poster.poster)
                .into(imgPoster)

            container.addView(view, 0)
        }


        return view
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun getCount(): Int {
        return posterList.size
    }

    override fun destroyItem(container: ViewGroup, position: Int, view: Any) {
        container.removeView(view as View)
    }
}