package com.proyek2.blkindramayu.adapter

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.proyek2.blkindramayu.BuildConfig
import com.proyek2.blkindramayu.R
import com.proyek2.blkindramayu.model.DataPelatihan
import com.proyek2.blkindramayu.model.Sertifikat
import com.proyek2.blkindramayu.network.NetworkConfig
import kotlinx.android.synthetic.main.item_member_datapelatihan.view.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdapterPelatihanByMember (pelatihan : List<DataPelatihan>, val context : Context, val dialogSertifikat : Dialog) : RecyclerView.Adapter<AdapterPelatihanByMember.ViewHolder>() {

    private var pelatihan = ArrayList(pelatihan)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_member_datapelatihan, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return pelatihan.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(pelatihan[position], context, dialogSertifikat)
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bindView(dataPelatihan: DataPelatihan, context: Context, dialogSertifikat: Dialog){
            itemView.tvNamaProgram.text = dataPelatihan.namaProgram
            itemView.tvGelombang.text = dataPelatihan.namaGelombang
            itemView.tvKuota.text = "Kuota ${dataPelatihan.kuota.toString()}"
            itemView.tvPendaftaran.text = "Pendaftaran ${dataPelatihan.tglAwalPendaftaran} - ${dataPelatihan.tglAkhirPendaftaran}"
            itemView.tvSeleksi.text = "Seleksi ${dataPelatihan.tglSeleksi}"
            itemView.tvPelaksanaan.text = "Pelaksanaan ${dataPelatihan.tglAwalPelaksanaan} - ${dataPelatihan.tglAkhirPelaksanaan}"
            when {
                dataPelatihan.status == 0 -> {
                    itemView.tvStatus.text = "Waiting List"
                    itemView.cvStatus.setCardBackgroundColor(Color.parseColor("#f9a825"))
                    Glide.with(context)
                        .load(R.drawable.ic_history_white_24dp)
                        .into(itemView.imgStatus)
                    itemView.btnSertifikat.visibility = View.GONE
                }
                dataPelatihan.status == 1 -> {
                    itemView.tvStatus.text = "Terdaftar"
                    itemView.cvStatus.setCardBackgroundColor(Color.parseColor("#03a9f4"))
                    Glide.with(context)
                        .load(R.drawable.ic_account_24dp)
                        .into(itemView.imgStatus)
                    itemView.btnSertifikat.visibility = View.GONE
                }
                dataPelatihan.status == 2 -> {
                    itemView.tvStatus.text = "Kompeten"
                    itemView.cvStatus.setCardBackgroundColor(Color.parseColor("#82cc00"))
                    Glide.with(context)
                        .load(R.drawable.ic_done_white_24dp)
                        .into(itemView.imgStatus)
                    itemView.btnSertifikat.visibility = View.VISIBLE
                    itemView.btnSertifikat.setOnClickListener {
                        //Toast.makeText(context, dataPelatihan.kdPendaftaran.toString(), Toast.LENGTH_LONG).show()
                        NetworkConfig().api().getSertifikatMember(dataPelatihan.kdPendaftaran!!).enqueue(object : Callback<Sertifikat>{
                            override fun onFailure(call: Call<Sertifikat>, t: Throwable) {
                                Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
                            }

                            override fun onResponse(
                                call: Call<Sertifikat>,
                                response: Response<Sertifikat>
                            ) {
                                if(response.isSuccessful){
                                    val data = response.body()?.data
                                    if(data?.isEmpty()!!){
                                        Toast.makeText(context, "Data Kosong" ,Toast.LENGTH_SHORT).show()
                                    }else{
                                        dialogSertifikat.setContentView(R.layout.item_sertifikat)
                                        val btnUnduh = dialogSertifikat.findViewById<Button>(R.id.btnUnduh)
                                        val btnClose = dialogSertifikat.findViewById<Button>(R.id.btnClose)
                                        val imgSertifikat= dialogSertifikat.findViewById<ImageView>(R.id.imgSertifikat)

                                        Glide.with(context)
                                            .load(data[0]?.gambar_sertifikat)
                                            //.load("${BuildConfig.IMAGE}/${data[0]?.gambar_sertifikat}")
                                            //.load("${BuildConfig.CLOUDIMAGES}/${data[0]?.gambar_sertifikat}")
                                            .into(imgSertifikat)

                                        btnClose.setOnClickListener { dialogSertifikat.dismiss() }
                                        btnUnduh.setOnClickListener {
                                            //Toast.makeText(context, "UNDUH", Toast.LENGTH_SHORT).show()

                                            NetworkConfig().api().downloadFileWithDynamicUrlAsync(
                                                data[0]?.gambar_sertifikat!!
                                                //"${BuildConfig.IMAGE}/${data[0]?.gambar_sertifikat}"
                                            )
                                                .enqueue(object : Callback<ResponseBody>{
                                                    override fun onFailure(
                                                        call: Call<ResponseBody>,
                                                        t: Throwable
                                                    ) {
                                                        Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
                                                    }

                                                    @SuppressLint("WrongConstant")
                                                    override fun onResponse(
                                                        call: Call<ResponseBody>,
                                                        response: Response<ResponseBody>
                                                    ) {
                                                        if(response.isSuccessful){
                                                            //val downloadImage = AsyncTaskDownloadImage(response.body()!!).execute()
                                                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                                                                if(ContextCompat.checkSelfPermission(context,Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
//                                                                    ActivityCompat.requestPermissions(
//                                                                        activity,
//                                                                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1000)
                                                                    Toast.makeText(context, "Izin Ditolak!", Toast.LENGTH_LONG).show()
                                                                }else{
                                                                    startDownloading(data[0]?.gambar_sertifikat!!, context, dataPelatihan.kdPendaftaran)
                                                                }
                                                            }else{
                                                               startDownloading(data[0]?.gambar_sertifikat!!, context, dataPelatihan.kdPendaftaran)
                                                            }

                                                        }else{
                                                            Toast.makeText(context, response.message(), Toast.LENGTH_LONG).show()
                                                        }
                                                    }

                                                })

                                        }

                                        dialogSertifikat.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                                        dialogSertifikat.window!!.attributes.windowAnimations = R.style.DialogAnimation
                                        dialogSertifikat.setCancelable(false)
                                        dialogSertifikat.show()
                                    }
                                }else{
                                    Toast.makeText(context, response.message(), Toast.LENGTH_LONG).show()
                                }
                            }

                        })

                    }
                }
                else -> {
                    itemView.tvStatus.text = "Tidak Kompeten"
                    itemView.cvStatus.setCardBackgroundColor(Color.parseColor("#e57373"))
                    Glide.with(context)
                        .load(R.drawable.ic_close_white_24dp)
                        .into(itemView.imgStatus)
                    itemView.btnSertifikat.visibility = View.GONE
                }
            }
        }

        fun startDownloading(url : String, context: Context, kd : Int){
            val request = DownloadManager.Request(Uri.parse(url))
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            request.setTitle("BLK-Sertifikat-${kd}.jpg")
            request.setDescription("The file is downloading...")

            //request.allowScanningByMediaScanner()
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "${System.currentTimeMillis()}")

            val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            manager.enqueue(request)

            Toast.makeText(context, "Unduh Berhasil", Toast.LENGTH_LONG).show()
        }
    }

}