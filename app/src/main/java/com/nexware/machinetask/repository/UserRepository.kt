package com.nexware.machinetask.repository

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.nexware.machinetask.model.ErrorModel
import com.nexware.machinetask.model.UserModel
import com.nexware.machinetask.network.NetworkResponseCallback
import com.nexware.machinetask.network.RestClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository private constructor() {
    private lateinit var mCallback: NetworkResponseCallback

    companion object {
        private var mInstance: UserRepository? = null
        fun getInstance(): UserRepository {
            if (mInstance == null) {
                synchronized(this) {
                    mInstance = UserRepository()
                }
            }
            return mInstance!!
        }
    }

    fun getUser(
        callback: NetworkResponseCallback,
    ): MutableLiveData<UserModel> {

        mCallback = callback
        val userData: MutableLiveData<UserModel> = MutableLiveData()
        val call = RestClient.getInstance().getApiService().getUser()
        call.enqueue(object : Callback<UserModel> {
            override fun onResponse(
                call: Call<UserModel>,
                response: Response<UserModel>
            ) {
                if (response.isSuccessful) {
                    userData.postValue(response.body()!!)
                    mCallback.onNetworkSuccess()
                } else {
                    val error = Gson()
                    val commonerror =
                        error.fromJson(response.errorBody()!!.charStream(), ErrorModel::class.java)
                    mCallback.onNetworkFailed(commonerror.error)
                }
            }

            override fun onFailure(
                call: Call<UserModel>,
                t: Throwable
            ) {
                mCallback.onNetworkFailure(t)
            }
        })
        return userData
    }

}