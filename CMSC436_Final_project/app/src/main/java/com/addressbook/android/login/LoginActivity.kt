package com.addressbook.android.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import com.addressbook.android.R
import com.addressbook.android.api.ApiRequest
import com.addressbook.android.api.NetworkStatus
import com.addressbook.android.databinding.ActivityLoginBinding
import com.addressbook.android.login.viewModel.LoginViewModel
import com.addressbook.android.main.AddressBookListingActivity
import com.addressbook.android.model.LoginUserResponse
import com.addressbook.android.model.UserLoginDetail
import com.addressbook.android.util.BaseAppCompatActivity
import com.addressbook.android.util.ConnectionDetector
import com.addressbook.android.util.Globals
import com.addressbook.android.util.SharedPrefsHelper
import com.addressbook.android.util.UtilsValidation
import com.addressbook.android.util.openActivity
import com.addressbook.android.util.toast
import com.facebook.CallbackManager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds

//An activity that handles user login. It provides functionalities for both traditional and Facebook logins,
// including input validation and user feedback.
class LoginActivity : BaseAppCompatActivity(), View.OnClickListener {

    //Facebook
    private var callbackManager: CallbackManager? = null
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    private val currentContext = this@LoginActivity
    private lateinit var mAdView: AdView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        MobileAds.initialize(this) {}
        setupBannerAd()
        init()
        setUpObserver()
    }

    private fun setupBannerAd() {
        // Reference to the AdView from the binding
        mAdView = binding.adView

        // Create an ad request
        val adRequest = AdRequest.Builder().build()

        // Load the ad request into the adView
        mAdView.loadAd(adRequest)
    }

    private fun init() {
        binding.apply {
            setSupportActionBar(toolbar.toolbar)
            toolbar.toolbarTitle.text = getString(R.string.title_login)
            btnLogin.setOnClickListener(currentContext)
            btnLoginFb.setOnClickListener(currentContext)
        }
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
    }

    private fun doRequestForLogin() {
        val body = ApiRequest.LoginBody(
            binding.etEmail.text.toString().trim(), binding.etPassword.text.toString().trim()
        )
        viewModel.loginUser(body)
    }

    private fun setUpObserver() {
        viewModel.getLoginData().observe(this) {
            when (it) {
                is NetworkStatus.Failed -> {
                    toast(it.msg)
                    Globals.showProgressDialog(this)
                }
                is NetworkStatus.NoInternet -> {
                    Globals.dismissDialog()
                }
                is NetworkStatus.Running -> {
                    Globals.showProgressDialog(this)
                }
                is NetworkStatus.Success -> {
                    Globals.dismissDialog()
                    val loginData = it.data as LoginUserResponse.Data
                    val userLoginDetail = UserLoginDetail(loginData.password, loginData.email)
                    SharedPrefsHelper.setUserDetails(userLoginDetail)
                    intentAddressBookListing()
                }
            }
        }
    }

    override fun onClick(view: View?) {
        currentContext.apply {
            Globals.hideKeyboard(this)
            when (view?.id) {
                R.id.btn_login -> {
                    if (isValid()) {
                        if (ConnectionDetector.internetCheck(this, true)) doRequestForLogin()
                    }
                }
                R.id.btn_login_fb -> {
                    if (ConnectionDetector.internetCheck(this, true)) fBLogin()
                }
            }
        }
    }

    private fun fBLogin() {
        viewModel.fBLogin(this).observe(this) {
            if (it.accessToken != null) {
                intentAddressBookListing()
            }
        }
    }

    private fun intentAddressBookListing() {
        openActivity(AddressBookListingActivity::class.java)
        finish()
    }

    private fun isValid(): Boolean {
        if (UtilsValidation.validateEmptyEditText(binding.etEmail)) {
            toast(getString(R.string.toast_err_email))
            requestFocus(binding.etEmail)
            return false
        }
        if (UtilsValidation.validateEmail(binding.etEmail)) {
            toast(getString(R.string.toast_err_enter_valid_email))
            requestFocus(binding.etEmail)
            return false
        }
        if (UtilsValidation.validateEmptyEditText(binding.etPassword)) {
            toast(getString(R.string.toast_err_password))
            requestFocus(binding.etPassword)
            return false
        }
        return true
    }

    private fun requestFocus(view: View) {
        if (view.requestFocus()) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager?.onActivityResult(requestCode, resultCode, data)
    }

    override fun onPause() {
        super.onPause()
        mAdView.pause()
    }

    override fun onResume() {
        super.onResume()
        mAdView.resume()
    }

    override fun onDestroy() {
        mAdView.destroy()
        super.onDestroy()
    }

}