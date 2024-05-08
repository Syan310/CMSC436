package com.addressbook.android.api

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException


//class to detect network connectivity
class ConnectivityInterceptor(val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isOnline(context)) {
            throw NoConnectivityException()
        }

        val builder = chain.request().newBuilder()
        return chain.proceed(builder.build())
    }

    //check if user is online or not
    private fun isOnline(context: Context?): Boolean {
        if (context == null) return false
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            try {
                val activeNetworkInfo = connectivityManager.activeNetworkInfo
                if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                    Log.i("update_statut", "Network is available : true")
                    return true
                }
            } catch (e: Exception) {
                Log.i("update_statut", "" + e.message)
            }
        }
        Log.i("update_statut", "Network is available : FALSE ")
        return false
    }
 /*   //check if user is online or not
    private fun isOnline(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)

        return if (connectivityManager is ConnectivityManager) {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                val networkInfo = connectivityManager.activeNetworkInfo
                networkInfo?.isConnected ?: false
            } else {
                val networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ?: false || networkCapabilities?.hasTransport(
                    NetworkCapabilities.TRANSPORT_WIFI
                ) ?: false

            }
        } else false
    }*/
}

//throw error for connectivity exception
class NoConnectivityException : IOException() {
    override val message: String
        get() = "No network available, please check your WiFi or Internet connection"
}