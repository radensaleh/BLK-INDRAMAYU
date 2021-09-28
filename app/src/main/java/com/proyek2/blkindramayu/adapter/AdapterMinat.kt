package com.proyek2.blkindramayu.adapter

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.proyek2.blkindramayu.R
import com.proyek2.blkindramayu.model.DataMinat
import kotlinx.android.synthetic.main.item_minat.view.*

class AdapterMinat(private val minatLists : List<DataMinat>, val context : Context, private val dialog : Dialog, private val btnOK : Button, private val tvMinat : TextView?, private val minatPilihan : List<DataMinat>?) : RecyclerView.Adapter<AdapterMinat.ViewHolder>(){

    var dataID : InterfaceID? = null

    private var pilihanID = ArrayList<Int>()
    private var pilihanMinat = ArrayList<String>()

    interface InterfaceID { fun getID(pilihanID : ArrayList<Int>) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_minat, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return minatLists.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.cbMinat.text = minatLists[position].minat

        if(minatPilihan != null){
            //update minat ketika sudah pnya akun
            for(i in minatPilihan.indices){
                if(minatPilihan[i].id_minat == minatLists[position].id_minat){
                    pilihanID.add(minatLists[i].id_minat!!)
                    pilihanMinat.add(minatLists[i].minat!!)

                    holder.itemView.cbMinat.isChecked = true
                }
            }
            dataID?.getID(pilihanID)

            holder.itemView.cbMinat.setOnCheckedChangeListener { _, b ->
                if(b){
                    pilihanID.add(minatLists[position].id_minat!!)
                    pilihanMinat.add(minatLists[position].minat!!)

                    dataID?.getID(pilihanID)
                }else{
                    var i = 0
                    while(i < pilihanID.size){
                        if(pilihanID[i] == minatLists[position].id_minat){
                            pilihanID.remove(pilihanID[i])
                            pilihanMinat.remove(pilihanMinat[i])
                        }
                        i++
                    }
                    dataID?.getID(pilihanID)
                }
            }
        }else{
            //registrasi
            holder.itemView.cbMinat.setOnCheckedChangeListener { _, b ->
                if(b){
                    pilihanID.add(minatLists[position].id_minat!!)
                    pilihanMinat.add(minatLists[position].minat!!)
                }else{
                    var i = 0
                    while(i < pilihanID.size){
                        if(pilihanID[i] == minatLists[position].id_minat){
                            pilihanID.remove(pilihanID[i])
                            pilihanMinat.remove(pilihanMinat[i])
                        }
                        i++
                    }

                }
            }

            btnOK.setOnClickListener {
                dialog.dismiss()
                tvMinat?.text = "Minat : $pilihanMinat" //set Text

                //pass to activity
                dataID?.getID(pilihanID)
            }
        }



    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //fun bindView(dataMinat: DataMinat, context: Context) { }
    }

}