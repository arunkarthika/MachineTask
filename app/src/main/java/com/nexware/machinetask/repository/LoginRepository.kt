package com.nexware.machinetask.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.nexware.machinetask.model.ErrorModel
import com.nexware.machinetask.model.LoginModel
import com.nexware.machinetask.network.NetworkResponseCallback
import com.nexware.machinetask.network.RestClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginRepository private constructor(){
    private lateinit var mCallback: NetworkResponseCallback
    companion object {
        private var mInstance: LoginRepository? = null
        fun getInstance(): LoginRepository {
            if (mInstance == null) {
                synchronized(this) {
                    mInstance = LoginRepository()
                }
            }
            return mInstance!!
        }
    }
    fun login(
        callback: NetworkResponseCallback,
        data: JsonObject,
    ): MutableLiveData<LoginModel> {

        mCallback = callback
        val login_data: MutableLiveData<LoginModel> = MutableLiveData()
        val login_call = RestClient.getInstance().getApiService().login(data)
        login_call.enqueue(object : Callback<LoginModel> {
            override fun onResponse(
                call: Call<LoginModel>,
                response: Response<LoginModel>
            ) {
                if (response.isSuccessful) {
                    login_data.postValue(response.body()!!)
                    mCallback.onNetworkSuccess()
                } else {
                    val error= Gson()
                    val commonerror=error.fromJson(response.errorBody()!!.charStream(), ErrorModel::class.java)
                    mCallback.onNetworkFailed(commonerror.error)
                }
            }

            override fun onFailure(
                call: Call<LoginModel>,
                t: Throwable
            ) {
                mCallback.onNetworkFailure(t)
            }
        })
        return login_data
    }


}