package com.application.studyroom.utils

import android.app.Activity
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun String.isValidEmail() = Patterns.EMAIL_ADDRESS.matcher(this).matches()


inline fun <reified T : Activity> Activity.startNewActivity(
    noinline intentBuilder: Intent.() -> Unit = {}
) {
    val intent = Intent(this, T::class.java).apply(intentBuilder)
    startActivity(intent)
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

fun TextInputLayout.setErrorHint(errorString: String) {
    error = errorString
    isErrorEnabled = true
}

fun TextInputLayout.resetErrorHint(errorString: String? = null) {
    error = ""
    isErrorEnabled = false
}

fun Long.formatTimestamp(): String {
    val timestamp = this
    val date = Date(timestamp)
    val now = System.currentTimeMillis()
    val diff = now - timestamp

    return when {
        diff < 60000 -> "Just now" // Less than 1 minute
        diff < 3600000 -> "${diff / 60000} min ago" // Less than 1 hour
        diff < 86400000 -> { // Less than 24 hours
            val format = SimpleDateFormat("h:mm a", Locale.getDefault())
            "Today ${format.format(date)}"
        }

        diff < 172800000 -> { // Less than 48 hours
            val format = SimpleDateFormat("h:mm a", Locale.getDefault())
            "Yesterday ${format.format(date)}"
        }

        else -> {
            val format = SimpleDateFormat("MMM dd, h:mm a", Locale.getDefault())
            format.format(date)
        }
    }
}

