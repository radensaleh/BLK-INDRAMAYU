package com.proyek2.blkindramayu.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.proyek2.blkindramayu.BuildConfig
import com.proyek2.blkindramayu.R
import com.proyek2.blkindramayu.activity.DetailInfoActivity
import com.proyek2.blkindramayu.model.DataInfo
import kotlinx.android.synthetic.main.item_info.view.*

class AdapterInfo(private val infoLists : List<DataInfo>, val context : Context) : RecyclerView.Adapter<AdapterInfo.ViewHolder>(){
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(get: DataInfo, context: Context){
            itemView.tvJudul.text = get.judul
            itemView.tvTgl.text = get.tgl_upload

            Glide.with(context)
                .load("${BuildConfig.IMAGE}/konten/${get.foto}")
                .into(itemView.imgFoto)

            itemView.cvInfo.setOnClickListener {
                val intent = Intent(context, DetailInfoActivity::class.java)
                intent.putExtra("id", get.id)
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_info, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
       return infoLists.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(infoLists[position], context)
    }

}