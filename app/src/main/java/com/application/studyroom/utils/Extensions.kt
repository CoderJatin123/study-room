package com.application.studyroom.utils

import android.content.Context
import android.content.Intent
import android.util.Patterns
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.application.studyroom.R
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textview.MaterialTextView

fun String.isValidEmail() = Patterns.EMAIL_ADDRESS.matcher(this).matches()


fun <T> AppCompatActivity.startNewActivity(target: Class<T>) {
    Intent(this, target).also {
        startActivity(it)
    }
}

fun showSnakeBar(view: View, msg: String) = Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show()

fun MaterialTextView.setData(
    value: String = ContextCompat.getString(context, R.string.app_name),
    isLoading: Boolean = false
) {
    foreground = if (isLoading) {
        ContextCompat.getDrawable(context, R.drawable.bg_shimmer)
    } else {
        text = value
        null
    }
}

fun ImageView.setLoading(
    isLoading: Boolean = false
) {
    if (isLoading) {
        foreground = ContextCompat.getDrawable(context, R.drawable.bg_shimmer)
    } else {
        foreground = null
    }
}

fun Context.showToast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun TextInputLayout.setErrorHint(errorString: String){
        error = errorString
        isErrorEnabled = true
}
fun TextInputLayout.resetErrorHint(errorString: String?=null){
        error = ""
        isErrorEnabled = false
}

