package com.proyek2.blkindramayu.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.proyek2.blkindramayu.model.Info
import com.proyek2.blkindramayu.network.NetworkConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BeritaViewModel : ViewModel() {

    private var data = MutableLiveData<Info>()
    private var status = MutableLiveData<Boolean>()

    init {
        getData()
    }

    private fun getData(){
        status.value = true

        NetworkConfig().api().getBerita().enqueue(object :Callback<Info>{
            override fun onFailure(call: Call<Info>, t: Throwable) {
                status.value = true
                data.value = null
            }

            override fun onResponse(call: Call<Info>, response: Response<Info>) {
                if(response.isSuccessful){
                    status.value = false
                    data.value = response.body()
                }else{
                    status.value = true
                }

            }

        })
    }

    fun setData() : MutableLiveData<Info>{
        return data
    }

    fun getStatus() : MutableLiveData<Boolean>{
        return status
    }

}