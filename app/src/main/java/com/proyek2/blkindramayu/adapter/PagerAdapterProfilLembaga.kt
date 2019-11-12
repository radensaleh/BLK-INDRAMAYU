package com.proyek2.blkindramayu.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.proyek2.blkindramayu.BuildConfig
import com.proyek2.blkindramayu.R
import com.proyek2.blkindramayu.model.DataProfilLembaga

class PagerAdapterProfilLembaga(val profilList : List<DataProfilLembaga>, val context : Context) : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(container.context).inflate(R.layout.item_profil_lembaga, container, false)
        val profil : DataProfilLembaga = profilList[position]

        val imgLembaga = view.findViewById<ImageView>(R.id.imgProfilLembaga)

        Glide.with(context)
            .load(BuildConfig.IMAGE + profil.image)
            .into(imgLembaga)

        container.addView(view, 0)

        return view
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun getCount(): Int {
        return profilList.size
    }

    override fun destroyItem(container: ViewGroup, position: Int, view: Any) {
        container.removeView(view as View)
    }
}