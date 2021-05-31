package com.nexware.machinetask.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.nexware.machinetask.model.UserModel
import com.nexware.machinetask.network.NetworkResponseCallback
import com.nexware.machinetask.repository.UserRepository
import com.nexware.machinetask.util.NetworkHelper


class UserViewModel(private val app: Application) : AndroidViewModel(app) {
    val mShowProgressBar = MutableLiveData(false)
    val mShowNetworkError: MutableLiveData<Boolean> = MutableLiveData()
    val mShowApiError = MutableLiveData<String>()
    private var mRepository = UserRepository.getInstance()
    private var data = MutableLiveData<UserModel>()

    fun getUser(): MutableLiveData<UserModel> {
        if (NetworkHelper.isOnline(app.baseContext)) {
            mShowProgressBar.value = true
            data = mRepository.getUser(object : NetworkResponseCallback {
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
            })
        } else {
            mShowProgressBar.value = false
            mShowNetworkError.value = true
        }
        return data
    }
}