package com.proyek2.blkindramayu.adapter

import android.annotation.SuppressLint
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
import kotlin.collections.ArrayList

@Suppress("NAME_SHADOWING")
class AdapterInfo(infoLists : List<DataInfo>, val context : Context, val tipe : Int) : RecyclerView.Adapter<AdapterInfo.ViewHolder>(){
    var array = ArrayList(infoLists)
    private val tempArray = ArrayList(array)

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(get: DataInfo, context: Context, tipe : Int){
            itemView.tvJudul.text = get.judul
            itemView.tvTgl.text = get.tgl_upload

            Glide.with(context)
                .load("${BuildConfig.IMAGE}/konten/${get.foto}")
                .into(itemView.imgFoto)

            itemView.cvInfo.setOnClickListener {
                val intent = Intent(context, DetailInfoActivity::class.java)
                intent.putExtra("id", get.id)
                intent.putExtra("tipe", tipe)
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_info, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
       return array.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(array[position], context, tipe)
    }

    @SuppressLint("DefaultLocale")
    fun filter(charText : String){
        var charText = charText
        charText = charText.toLowerCase()
        array.clear()

        if(charText.isEmpty()){
           array.addAll(tempArray)
        }else{
            for(i in tempArray.indices){
                if(tempArray[i].judul!!.toLowerCase().contains(charText) || tempArray[i].tgl_upload!!.toLowerCase().contains(charText)){
                    array.add(tempArray[i])
                }
            }
        }
        notifyDataSetChanged()
    }

}