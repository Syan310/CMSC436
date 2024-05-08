package com.addressbook.android.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.InputFilter
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.addressbook.android.R
import java.io.Serializable


fun Activity.toast(message: String) {
    if (message.isNotEmpty()) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

fun Context.toast(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun Context.getColorCompat(@ColorRes resourceId: Int) = ContextCompat.getColor(this, resourceId)

fun View.setViewClickable(isNeedToClick: Boolean) {
    this.isClickable = isNeedToClick
    this.isEnabled = isNeedToClick
}

fun View.enable() {
    this.isClickable = true
    this.isEnabled = true
}

fun View.disable() {
    this.isClickable = false
    this.isEnabled = false
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.hide() {
    visibility = View.GONE
}


fun View.handleVisibleHide(b: Boolean) {
    visibility = if(b) View.VISIBLE else View.GONE
}


fun EditText.clear() {
    text.clear()
}

fun TextView.clear() {
    text = ""
}

fun AppCompatEditText.textGet() {
    this.text.toString()
}

fun <T> Context.openActivity(it: Class<T>) {
    val intent = Intent(this, it)
    (this as Activity).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out)
    startActivity(intent)
}

fun <T> Context.openActivityWithIntent(it: Class<T>, key: String, value: Serializable) {
    val intent = Intent(this, it)
    intent.putExtra(key, value)
    (this as Activity).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out)
    startActivity(intent)
}

// extension function to set edit text maximum length
fun AppCompatEditText.setMaxLength(maxLength: Int) {
    filters = arrayOf<InputFilter>(InputFilter.LengthFilter(maxLength))
}




