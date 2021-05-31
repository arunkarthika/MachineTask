package com.nextgen.machinetask.network

import com.google.gson.JsonObject
import com.nexware.machinetask.model.LoginModel
import com.nexware.machinetask.model.UserModel
import retrofit2.Call
import retrofit2.http.*

interface ApiServices {
    @Headers("Content-Type: application/json")
    @POST("login")
    fun login(@Body jsonObject: JsonObject): Call<LoginModel>

    @GET("users")
    fun getUser():Call<UserModel>
}