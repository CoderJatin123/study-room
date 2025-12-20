package com.application.studyroom

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        onBackPressedDispatcher.addCallback(onBackPressCallback)
        super.onCreate(savedInstanceState)
    }

    val onBackPressCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            onBaseBackPressed()
        }
    }

    open fun onBaseBackPressed() {
        finish()
    }
}