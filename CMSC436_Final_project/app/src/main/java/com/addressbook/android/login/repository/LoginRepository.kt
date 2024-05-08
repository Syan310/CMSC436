package com.addressbook.android.login.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.addressbook.android.api.ApiClient
import com.addressbook.android.api.ApiRequest
import com.addressbook.android.api.BaseRepository
import com.addressbook.android.api.NetworkStatus
import com.addressbook.android.login.LoginActivity
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult


// This repository class manages the login process.
// It includes methods for Facebook login and communicating with a custom login API.
// It handles and stores the results and status of these operations.
class LoginRepository(application: Application) : BaseRepository(application) {

    val loginResults = MutableLiveData<LoginResult>()
    private val responseData = MutableLiveData<NetworkStatus>()

    fun fBLogin(loginActivity: LoginActivity): LiveData<LoginResult> {

            val callbackManager = CallbackManager.Factory.create()

            // Set permissions
            LoginManager.getInstance().logInWithReadPermissions(loginActivity, listOf("public_profile", "email"))
            LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {

                override fun onSuccess(loginResult: LoginResult) {
                    loginResults.value = loginResult
                }

                override fun onCancel() {
                }

                override fun onError(error: FacebookException) {
                }
            })

        return loginResults
    }

    //this method called from LoginViewModel
    suspend fun doRequestForLoginUser(body: ApiRequest.LoginBody) {
        runApi(
            apiCall = {
                ApiClient.apiClient(application).login(body)
            }, apiStatus = responseData
        )
    }

    //pass this status to loginViewModel
    fun getLoginData() = responseData
}