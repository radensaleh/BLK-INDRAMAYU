package com.proyek2.blkindramayu.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.proyek2.blkindramayu.model.Res
import com.proyek2.blkindramayu.network.NetworkConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrasiViewModel : ViewModel(){
    private var data = MutableLiveData<Res>()
    private var status = MutableLiveData<Boolean>()
    private var res = MutableLiveData<String>()

    private fun registrasi(nik: String, nama: String, tmptLahir: Int, tglLahir: String, jk: String, idProvinsi: Int, idKota: Int, alamat: String, kodepos: Int, username: String, password: String, email: String, status_member : Int){
        status.value = true

        NetworkConfig().api().registrasiMember(nik,nama,tmptLahir,tglLahir,jk,idProvinsi,idKota,alamat,kodepos,username,password,email,status_member).enqueue(object : Callback<Res>{
            override fun onFailure(call: Call<Res>, t: Throwable) {
                status.value = true
                data.value = null
                res.value = t.message
            }

            override fun onResponse(call: Call<Res>, response: Response<Res>) {
                if(response.isSuccessful){
                    status.value = false
                    data.value = response.body()
                }else{
                    status.value = true
                }
            }

        })
    }

    fun setData(nik : String, nama : String, tmptLahir : Int, tglLahir : String, jk : String, idProvinsi : Int, idKota : Int, alamat : String, kodepos : Int, username : String, password : String, email : String, status : Int) : MutableLiveData<Res> {
        registrasi(nik,nama,tmptLahir,tglLahir,jk,idProvinsi,idKota,alamat,kodepos,username,password,email,status)

        return data
    }

    fun getResponse() : MutableLiveData<String> {
        return res
    }

}