package com.addressbook.android.util

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.provider.Settings
import com.addressbook.android.R

object ConnectionDetector {


    private fun isNetworkAvailable(context: Context?): Boolean {
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
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false
    }

    fun internetCheck(context: Context, showDialog: Boolean): Boolean {
        if (isNetworkAvailable(context))
            return true
        if (showDialog)
            showAlertDialog(context, context.getString(R.string.msg_NO_INTERNET_TITLE), context.getString(R.string.msg_NO_INTERNET_MSG), false)
        return false
    }

    private fun showAlertDialog(context: Context, pTitle: String, pMsg: String, status: Boolean?) {
        try {
            val builder = AlertDialog.Builder(context)

            builder.setTitle(pTitle)
            builder.setMessage(pMsg)
            builder.setCancelable(true)
            builder.setPositiveButton(context.getString(R.string.msg_goto_settings)
            ) { dialog, _ ->
                dialog.dismiss()
                val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                context.startActivity(intent)
            }
            val alert = builder.create()
            alert.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}