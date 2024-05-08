package com.addressbook.android.login

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.addressbook.android.databinding.ActivitySplashBinding
import com.addressbook.android.main.AddressBookListingActivity
import com.addressbook.android.util.*

class LauncherActivity : BaseAppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler(Looper.myLooper()!!).postDelayed({
            openNavigationActivity()
        }, 2000)
    }

    private fun openNavigationActivity() {
        val cls = if (SharedPrefsHelper.getUserDetails() == null) LoginActivity::class.java
        else AddressBookListingActivity::class.java

        openActivity(cls)
        finish()
    }
}
