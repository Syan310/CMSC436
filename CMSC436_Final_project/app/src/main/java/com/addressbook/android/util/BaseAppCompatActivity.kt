package com.addressbook.android.util

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.addressbook.android.R

open class BaseAppCompatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBackPressed()
    }

    private fun setupBackPressed() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                popBack()
            }
        })
    }

    private fun popBack() {
        Globals.hideKeyboard(this)
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out)
    }
}