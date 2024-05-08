package com.addressbook.android.model

import java.io.Serializable

//A simple, serializable data class that represents user login details, specifically containing the user's email and password.
data class UserLoginDetail(var Password: String, var Email: String) : Serializable




