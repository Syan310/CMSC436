package com.addressbook.android.api


//check api status
sealed class NetworkStatus {
    data class Running(val msg: String = "Running") : NetworkStatus()
    data class NoInternet(val msg: String = "No Internet") : NetworkStatus()
    data class Success(val msg: String = "", val apiName: String = "", val data: Any? = null,val data2: Any? = null, val other: Any? = null) : NetworkStatus()
    data class Failed(val msg: String = "Server is temporary down! Please try after sometime.", val id: Int? = 0) : NetworkStatus()
}


