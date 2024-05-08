package com.addressbook.android.util

import android.text.TextUtils
import android.util.Patterns
import androidx.appcompat.widget.AppCompatEditText
import java.util.regex.Matcher
import java.util.regex.Pattern

object UtilsValidation {

    fun validateEmptyEditText(et_advertise: AppCompatEditText): Boolean {
        return et_advertise.text.toString().trim { it <= ' ' }.isEmpty()
    }

    fun validateEmail(et_email: AppCompatEditText): Boolean {
        val email = et_email.text.toString().trim { it <= ' ' }
        return email.isEmpty() || !isValidEmail(email)
    }

    private fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidWebUrl(url: String): Boolean {
        return !TextUtils.isEmpty(url) && !Patterns.WEB_URL.matcher(url).matches()
    }


    fun validatePassword(et_password: AppCompatEditText): Boolean {
        val password = et_password.text.toString().trim { it <= ' ' }
        return password.isEmpty() || !isValidPassword(password)
    }


    fun validateConfirmPassword(et_password: AppCompatEditText, et_confirm_password: AppCompatEditText): Boolean {
        return et_password.text.toString().trim { it <= ' ' } != et_confirm_password.text.toString().trim { it <= ' ' }
    }

    fun isValidPassword(password: String): Boolean {
        val pattern: Pattern
        val PASSWORD_PATTERN = "^.*(?=.{8,})(?=.*\\d)(?=.*[a-zA-Z]).*$"
        pattern = Pattern.compile(PASSWORD_PATTERN)
        val matcher: Matcher = pattern.matcher(password)
        return matcher.matches()
    }

    fun validatePhoneNumber(et_phone_number: AppCompatEditText): Boolean {
        val cardNumber = et_phone_number.text.toString().trim { it <= ' ' }
        return cardNumber.isEmpty() || cardNumber.length != 10
    }

}