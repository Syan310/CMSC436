package com.addressbook.android.login.viewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.addressbook.android.api.ApiRequest
import com.addressbook.android.api.NetworkStatus
import com.addressbook.android.api.NetworkViewModel
import com.addressbook.android.login.LoginActivity
import com.addressbook.android.login.repository.LoginRepository
import com.facebook.login.LoginResult
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : NetworkViewModel(application) {

    override val repository = LoginRepository(application)
    private val loginResults = MutableLiveData<LoginResult>()
    private var responseData: LiveData<NetworkStatus> = repository.getLoginData()

    fun fBLogin(loginActivity: LoginActivity): LiveData<LoginResult> {
        viewModelScope.launch {
            repository.fBLogin(loginActivity).observe(loginActivity, Observer {
                loginResults.value = it
            })
        }
        return loginResults
    }

    //pass data to repository for api call
    fun loginUser(body: ApiRequest.LoginBody) {
        viewModelScope.launch {
            repository.doRequestForLoginUser(body)
        }
    }

    // this method used for observe api call status
    fun getLoginData() = responseData
}