package com.proyek2.blkindramayu.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.proyek2.blkindramayu.model.Poster
import com.proyek2.blkindramayu.network.NetworkConfig
import retrofit2.Call
import retrofit2.Response

class SlidePosterViewModel : ViewModel() {

    private var status = MutableLiveData<Boolean>()
    private var data = MutableLiveData<Poster>()

    init {
        getData()
    }

    private fun getData(){

        status.value = true

        NetworkConfig().api().getPoster().enqueue(object :retrofit2.Callback<Poster>{
            override fun onFailure(call: Call<Poster>, t: Throwable) {
                status.value = true
                data.value = null
            }

            override fun onResponse(call: Call<Poster>, response: Response<Poster>) {
                if (response.isSuccessful) {
                    status.value = false
                    data.value = response.body()
                }else {
                    status.value = true
                }
            }

        })

    }

    fun setData() : MutableLiveData<Poster>{
        return data
    }

    fun getStatus() : MutableLiveData<Boolean>{
        return status
    }
}