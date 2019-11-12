package com.proyek2.blkindramayu.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.proyek2.blkindramayu.model.Res
import com.proyek2.blkindramayu.network.NetworkConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {
    private var data = MutableLiveData<Res>()
    private var status = MutableLiveData<Boolean>()

    private lateinit var user : String
    private lateinit var pass : String

    private fun login(){
        status.value = true

        NetworkConfig().api().loginMember(user, pass).enqueue(object : Callback<Res>{
            override fun onFailure(call: Call<Res>, t: Throwable) {
                status.value = true
                data.value = null
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

    fun setData(username : String, password : String) : MutableLiveData<Res>{
        this.user = username
        this.pass = password

        login()

        return data
    }

    fun getStatus() : MutableLiveData<Boolean>{
        return status
    }

}