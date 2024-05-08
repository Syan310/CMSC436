package com.addressbook.android.util

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.widget.AppCompatEditText
import com.addressbook.android.login.LoginActivity
import com.addressbook.android.model.UserLoginDetail
import com.facebook.login.LoginManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object SharedPrefsHelper {

    private const val SHARED_PREFS_NAME = Constant.MM_secrets

    private var sharedPreferences: SharedPreferences = App.instance.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)

    private fun getEditor(): SharedPreferences.Editor {
        return sharedPreferences.edit()
    }

    // storing model class in preference
    private fun toJsonString(params: UserLoginDetail?): String? {
        if (params == null) {
            return null
        }
        val mapType = object : TypeToken<UserLoginDetail>() {

        }.type
        val gson = Gson()
        return gson.toJson(params, mapType)
    }

    private fun toUserDetails(params: String?): UserLoginDetail? {
        if (params == null)
            return null

        val mapType = object : TypeToken<UserLoginDetail>() {

        }.type
        val gson = Gson()
        return gson.fromJson(params, mapType)
    }

    fun setUserDetails(userMap: UserLoginDetail?) {
        getEditor().putString(Constant.AB_USER_MAP, toJsonString(userMap))
        getEditor().commit()
    }

    fun getUserDetails(): UserLoginDetail? {
        return toUserDetails(sharedPreferences.getString(Constant.AB_USER_MAP, null))
    }

    fun trimString(textView: AppCompatEditText): String {
        return textView.text.toString().trim { it <= ' ' }
    }
}