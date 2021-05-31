package com.nexware.machinetask.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.nexware.machinetask.model.LoginModel
import com.nexware.machinetask.network.NetworkResponseCallback
import com.nexware.machinetask.repository.LoginRepository
import com.nexware.machinetask.util.NetworkHelper


class LoginViewModel(private val app:Application):AndroidViewModel(app) {
    val mShowProgressBar = MutableLiveData(false)
    val mShowNetworkError: MutableLiveData<Boolean> = MutableLiveData()
    val mShowApiError = MutableLiveData<String>()
    private var mRepository = LoginRepository.getInstance()
    private  var loginData: MutableLiveData<LoginModel> = MutableLiveData<LoginModel>()

    fun login(login_body: JsonObject):MutableLiveData<LoginModel>{
        if (NetworkHelper.isOnline(app.baseContext)) {
            mShowProgressBar.value = true
            loginData=  mRepository.login(object : NetworkResponseCallback {
                override fun onNetworkSuccess() {
                    mShowProgressBar.value = false
                }
                override fun onNetworkFailed(msg: String) {
                    mShowProgressBar.value = false
                    mShowApiError.value = msg
                }
                override fun onNetworkFailure(th: Throwable) {
                    mShowProgressBar.value = false
                    mShowApiError.value = th.message
                }
            }, login_body)
        } else {
            mShowProgressBar.value = false
            mShowNetworkError.value = true
        }
        return loginData
    }
}