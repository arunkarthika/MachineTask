package com.nexware.machinetask.network

interface NetworkResponseCallback {
    fun onNetworkSuccess()
    fun onNetworkFailed(msg:String)
    fun onNetworkFailure(th : Throwable)
}