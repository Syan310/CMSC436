package com.addressbook.android.api

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.addressbook.android.model.LoginUserResponse
import com.addressbook.android.model.UserLoginDetail
import com.addressbook.android.util.App

import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.HttpException


//used this class for call api and get response
//change LoginUserResponse model to common response class

open class BaseRepository(val application: Application) {
    private val myMessage = "message"
    private val myError = "error"

    suspend inline fun <T> runApi(crossinline apiCall: suspend () -> T,
        apiStatus: MutableLiveData<NetworkStatus>, other: Any? = null) {
        application.applicationContext as App

        return try {
            //set apiStatus according to api status code
            apiStatus.postValue(NetworkStatus.Running())
            val apiResponse = apiCall.invoke()
            val responseData = (apiResponse as LoginUserResponse)
            apiStatus.postValue(NetworkStatus.Success( data = responseData.data))

        } catch (e: Exception) {
            checkConnectionError(e, apiStatus)
        }
    }

    fun checkConnectionError(e: Exception, apiStatus: MutableLiveData<NetworkStatus>) {
        when (e) {
            is HttpException -> {
                val body = e.response()?.errorBody()
                apiStatus.postValue(NetworkStatus.Failed(getErrorMessage(body), e.response()?.code()))
            }
            is KotlinNullPointerException ->
                apiStatus.postValue(NetworkStatus.Failed("failed to connect"))

            is NoConnectivityException ->
                apiStatus.postValue(NetworkStatus.NoInternet("failed to connect"))

            else -> {
                apiStatus.postValue(NetworkStatus.Failed("failed to connect"))
            }
        }
    }

    private fun getErrorMessage(responseBody: ResponseBody?): String {
        return try {
            val jsonObject = JSONObject(responseBody!!.string())
            when {
                jsonObject.has(myMessage) -> {
                    jsonObject.getString(myMessage)
                }
                jsonObject.has(myError) -> jsonObject.getString(myError)
                else -> "Something went wrong"
            }
        } catch (e: Exception) {
            if (responseBody?.string() == null || responseBody.string().isEmpty()) ""
            else "Something went wrong"
        }
    }
}

