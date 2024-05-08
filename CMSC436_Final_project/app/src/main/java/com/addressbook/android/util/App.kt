package com.addressbook.android.util

import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication

class App : MultiDexApplication() {

    companion object {
        lateinit var instance: App
    }

    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
        instance = this

    }
}