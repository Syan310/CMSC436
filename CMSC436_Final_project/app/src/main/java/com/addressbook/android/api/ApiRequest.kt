package com.addressbook.android.api


//convert data to request data
//here data pass as model but some time data pass in json format
object ApiRequest {
    data class LoginBody(var Email: String, var Password: String)
}