package com.proyek2.blkindramayu.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.proyek2.blkindramayu.model.ProfilLembaga
import com.proyek2.blkindramayu.network.NetworkConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfilLembagaViewModel : ViewModel() {

    private var data = MutableLiveData<ProfilLembaga>()
    private var status = MutableLiveData<Boolean>()

    init {
        getData()
    }

    private fun getData(){
        status.value = true

        NetworkConfig().api().getProfilLembaga().enqueue(object : Callback<ProfilLembaga>{
            override fun onFailure(call: Call<ProfilLembaga>, t: Throwable) {
                status.value = true
                data.value = null
            }

            override fun onResponse(call: Call<ProfilLembaga>, response: Response<ProfilLembaga>) {
                if(response.isSuccessful){
                    status.value = false
                    data.value = response.body()
                }else{
                    status.value = true
                }
            }

        })
    }

    fun setData() : MutableLiveData<ProfilLembaga>{
        return data
    }

    fun getStatus() : MutableLiveData<Boolean>{
        return status
    }

}