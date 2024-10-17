package com.example.job_test.utility

import android.content.Context
import android.content.SharedPreferences

object PreferenceManager {
    private const val PREF_NAME = "job_test"
    private const val TOKEN_KEY="token"

    private fun getPreference(context: Context):SharedPreferences{
        return context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE)
    }

    fun saveToken(context: Context,token:String){
        getPreference(context).edit().putString(TOKEN_KEY,token).apply()
    }

    fun getToken(context: Context):String?{
        return getPreference(context).getString(TOKEN_KEY,null)
    }

    fun clearToken(context: Context){
        getPreference(context).edit().remove(TOKEN_KEY).apply()
    }
}