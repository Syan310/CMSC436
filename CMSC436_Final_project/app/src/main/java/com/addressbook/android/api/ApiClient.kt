package com.addressbook.android.api

import android.content.Context
import com.addressbook.android.BuildConfig

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

//initialize retrofit here
object ApiClient {

    private var retrofit: Retrofit? = null

    //set retrofit object
    fun apiClient(context: Context): ApiInterface {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .client(getHttpClient(context))
                .baseUrl(ApiConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!.create(ApiInterface::class.java)
    }

    //get http client
    private fun getHttpClient(context: Context, isConnectionCheck: Boolean = false): OkHttpClient {
        val client: OkHttpClient
        val builder = OkHttpClient().newBuilder()

        if (BuildConfig.DEBUG) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(interceptor)
        }

        builder.addInterceptor(ConnectivityInterceptor(context))
        if (isConnectionCheck) {
            builder.readTimeout(30, TimeUnit.SECONDS).callTimeout(30, TimeUnit.SECONDS)
        }
        client = builder.build()
        return client
    }
}