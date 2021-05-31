package com.nexware.machinetask.util

import android.content.Context
import android.content.SharedPreferences

class SessionManager{
    private lateinit var mContext: Context
    lateinit var appConstants: AppConstant

    fun SessionManager(context: Context) {
        mContext = context
        appConstants= AppConstant
    }

    fun getSessionStringValue(sessionName: String, sessionKey: String): String {
        val settings = getSession(sessionName)
        return settings.getString(sessionKey, "0")!!
    }

    private fun getSession(sessionName: String): SharedPreferences {
        val PRIVATE_MODE = 0
        return mContext.getSharedPreferences(sessionName, PRIVATE_MODE)
    }

    fun getSessionIntValue(sessionName: String, sessionKey: String): Int {
        val settings = getSession(sessionName)
        // Reading integer value from SharedPreferences
        return settings.getInt(sessionKey, 0)
    }


    fun storeSessionStringvalue(sessionName: String, key: String, value: String) {
        val settings = getSession(sessionName)
        // Writing String data to SharedPreferences
        val editor = settings.edit()
        editor.putString(key, value)
        editor.apply()
    }


    fun storeSessionIntvalue(sessionName: String, key: String, value: Int) {
        val settings = getSession(sessionName)
        // Writing integer data to SharedPreferences
        val editor = settings.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun isLogin(): Boolean {
        val settings = getSession(appConstants.LOGIN_SESSION_NAME)
        // Reading integer value from SharedPreferences
        return settings.getInt(appConstants.LOGIN_SESSION_USER_ID, 0) > 0
    }

    fun getUserID(): String {
        val settings = getSession(appConstants.LOGIN_SESSION_NAME)
        // Reading integer value from SharedPreferences
//        Log.w("Success", "UserID::: " + settings.getInt(appConstants.LOGIN_SESSION_USER_ID, -1))
        return if (settings.getInt(appConstants.LOGIN_SESSION_USER_ID, -1) > 0) settings.getInt(appConstants.LOGIN_SESSION_USER_ID, -1).toString() else ""
    }

    fun callLogout() {
        val settings = getSession(appConstants.LOGIN_SESSION_NAME)
        val editor = settings.edit()
        editor.clear()
        editor.apply()
    }

}