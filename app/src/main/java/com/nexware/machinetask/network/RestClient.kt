package com.nexware.machinetask.network

import com.nextgen.machinetask.network.ApiServices
import com.nexware.machinetask.util.AppConstant
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RestClient private constructor() {

    private val REQUEST_TIMEOUT = 30
    private var logging = HttpLoggingInterceptor()

    private fun getHttpLogClient(): OkHttpClient {
        val httpClient = OkHttpClient().newBuilder()
            .connectTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .readTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .writeTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
            logging.level = HttpLoggingInterceptor.Level.BODY

        httpClient.addNetworkInterceptor(logging)
        return httpClient.build()
    }


    companion object {
        private lateinit var mApiServices: ApiServices
        private var mInstance: RestClient? = null
        fun getInstance(): RestClient {
            if (mInstance == null) {
                synchronized(this) {
                    mInstance = RestClient()
                }
            }
            return mInstance!!
        }
    }

    init {
        val retrofit = Retrofit.Builder().baseUrl(AppConstant.BaseUrl)
            .client(getHttpLogClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        mApiServices = retrofit.create(ApiServices::class.java)
    }
    fun getApiService() = mApiServices
}