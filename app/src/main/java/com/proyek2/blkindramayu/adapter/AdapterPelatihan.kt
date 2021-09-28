package com.proyek2.blkindramayu.adapter

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.proyek2.blkindramayu.R
import com.proyek2.blkindramayu.model.DataPelatihan
import com.proyek2.blkindramayu.model.Member
import com.proyek2.blkindramayu.model.Res
import com.proyek2.blkindramayu.network.NetworkConfig
import com.proyek2.blkindramayu.room.MemberEntity
import kotlinx.android.synthetic.main.item_pelatihan.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdapterPelatihan(pelatihan : List<DataPelatihan>, val context : Context, private val dialogDaftar : Dialog, val member: MemberEntity?, val loading : Dialog, val alertDialog: Dialog) : RecyclerView.Adapter<AdapterPelatihan.ViewHolder>(){

    private var pelatihan = ArrayList(pelatihan)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pelatihan, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return pelatihan.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(pelatihan[position], context, dialogDaftar, member, loading, alertDialog)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bindView(dataPelatihan: DataPelatihan, context: Context, dialogDaftar: Dialog, member: MemberEntity?, loading: Dialog, alertDialog: Dialog) {
            var data : List<Member?>? = null
            if(member != null){
                val kdPengguna = member.kd_pengguna
                val username = member.username

                NetworkConfig().api().getMember(kdPengguna, username).enqueue(object : Callback<Res>{
                    override fun onFailure(call: Call<Res>, t: Throwable) {
                        Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<Res>, response: Response<Res>) {
                        if(response.isSuccessful){
                            data = response.body()?.data_member
                        }else{
                            Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                        }
                    }

                })
            }

            itemView.tvNamaProgram.text = dataPelatihan.namaProgram
            itemView.tvGelombang.text = dataPelatihan.namaGelombang
            itemView.tvKuota.text = "Kuota ${dataPelatihan.kuota.toString()}"
            itemView.tvPendaftaran.text = "Pendaftaran ${dataPelatihan.tglAwalPendaftaran} - ${dataPelatihan.tglAkhirPendaftaran}"
            itemView.tvSeleksi.text = "Seleksi ${dataPelatihan.tglSeleksi}"
            itemView.tvPelaksanaan.text = "Pelaksanaan ${dataPelatihan.tglAwalPelaksanaan} - ${dataPelatihan.tglAkhirPelaksanaan}"
            itemView.cvGelombang.setOnClickListener {
                dialogDaftar.setContentView(R.layout.item_daftar_pelatihan)
                val btnClose = dialogDaftar.findViewById<Button>(R.id.btnClose)
                val btnDaftar = dialogDaftar.findViewById<Button>(R.id.btnDaftar)
                val judul = dialogDaftar.findViewById<TextView>(R.id.tvJudul)
                val deskripsi = dialogDaftar.findViewById<TextView>(R.id.tvDeskripsi)
                val waktuPendaftaran = dialogDaftar.findViewById<TextView>(R.id.tvPendaftaran)
                val waktuSeleksi = dialogDaftar.findViewById<TextView>(R.id.tvSeleksi)
                val waktuPelaksanaan = dialogDaftar.findViewById<TextView>(R.id.tvPelaksanaan)
                val kuota = dialogDaftar.findViewById<TextView>(R.id.tvKuota)

                judul.text = dataPelatihan.namaProgram
                //deskripsi.text = Html.fromHtml(Html.fromHtml(dataPelatihan.detailProgram).toString())
                waktuPendaftaran.text = "Pendaftaran ${dataPelatihan.tglAwalPendaftaran} - ${dataPelatihan.tglAkhirPendaftaran}"
                waktuSeleksi.text = "Seleksi ${dataPelatihan.tglSeleksi}"
                waktuPelaksanaan.text = "Pelaksanaan ${dataPelatihan.tglAwalPelaksanaan} - ${dataPelatihan.tglAkhirPelaksanaan}"
                kuota.text = "Kuota ${dataPelatihan.kuota.toString()}"

                btnClose.setOnClickListener {
                    dialogDaftar.dismiss()
                }

                btnDaftar.setOnClickListener {
                    if(member == null){
                        Snackbar.make(itemView, "Anda Belum Login", Snackbar.LENGTH_LONG).show()
                        dialogDaftar.dismiss()
                    }else{
                        if(data?.get(0)?.pend_terakhir == null || data?.get(0)?.thn_ijazah == null
                            || data?.get(0)?.nomor_kontak == null || data?.get(0)?.ukuran_baju == null || data?.get(0)?.ukuran_baju == null){
                            Snackbar.make(itemView, "Harap Lengkapi Data Diri Anda", Snackbar.LENGTH_LONG).show()
                            dialogDaftar.dismiss()
                        }else{

                            loading.setContentView(R.layout.loading)
                            loading.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                            loading.window!!.attributes.windowAnimations = R.style.DialogAnimation
                            loading.setCancelable(false)
                            loading.show()

                            Handler().postDelayed({
                                NetworkConfig().api().daftarPelatihan(dataPelatihan.kdSkema!!, member.kd_pengguna).enqueue(object : Callback<Res>{
                                    override fun onFailure(call: Call<Res>, t: Throwable) {
                                        loading.dismiss()
                                        dialogDaftar.dismiss()
                                        Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                                    }

                                    override fun onResponse(call: Call<Res>, response: Response<Res>) {
                                        loading.dismiss()
                                        if(response.isSuccessful){
                                            val message = response.body()?.message?.get(0)
                                            val status = response.body()?.status_code
                                            alertDialog(
                                                message!!,
                                                status!!, dialogDaftar, alertDialog, context)
                                        }else{
                                            dialogDaftar.dismiss()
                                            Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                                        }
                                    }

                                })

                            }, 1000)

                        }
                    }

                }

                dialogDaftar.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialogDaftar.window!!.attributes.windowAnimations = R.style.DialogAnimation
                dialogDaftar.setCancelable(false)
                dialogDaftar.show()
            }
        }

        @SuppressLint("SetTextI18n")
        private fun alertDialog(message : String, status_code : Int, dialog: Dialog, alertDialog: Dialog, context: Context){
            when (status_code) {
                200 -> alertDialog.setContentView(R.layout.alert_success)
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
                    200 -> {
                        dialog.dismiss()
//                        val intent = Intent(context, BerandaActivity::class.java)
//                        intent.putExtra("navTab", 4)
//                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//                        startActivity(intent)
                    }
                    else -> dialog.dismiss()
                }
            }

            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
            alertDialog.setCancelable(false)
            alertDialog.show()

        }

    }

}