package com.application.studyroom.utils

import android.content.Intent
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

fun String.isValidEmail() = Patterns.EMAIL_ADDRESS.matcher(this).matches()


fun <T> AppCompatActivity.startNewActivity(target: Class<T>) {
    Intent(this, target).also {
        startActivity(it)
    }
}

fun showSnakeBar(view: View, msg: String) = Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show()