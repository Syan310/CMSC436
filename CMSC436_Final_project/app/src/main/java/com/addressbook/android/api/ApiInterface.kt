package com.addressbook.android.api

import com.addressbook.android.model.LoginUserResponse
import com.addressbook.android.model.UserLoginDetail
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST


//Api call
//create common response class
//here only one api calling for login so i put login api response model
interface ApiInterface {
    @POST("post")
    suspend fun login(@Body body: ApiRequest.LoginBody): LoginUserResponse
}