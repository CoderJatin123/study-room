package com.application.studyroom.ui.validator

import com.application.studyroom.utils.isValidEmail
import com.google.android.material.textfield.TextInputLayout

object InputValidator {
    fun validateEmail(layout: TextInputLayout): Boolean {
        val c1 = validateInputField(layout)
        val c2 = layout.editText?.text?.trim().toString().isValidEmail()
        if (!c2) {
            layout.error = "Invalid email address"
            layout.isErrorEnabled = true
        } else {
            layout.error = ""
            layout.isErrorEnabled = false
        }
        return c1 && c2
    }

    private fun validateInputField(layout: TextInputLayout): Boolean {
        if (layout.editText?.text.toString().trim().isBlank()) {
            layout.error = "This field is empty"
            layout.isErrorEnabled = true
            return false
        } else {
            layout.error = ""
            layout.isErrorEnabled = false
            return true
        }
    }

    fun validatePassword(layout: TextInputLayout): Boolean {
        val c1 = validateInputField(layout)
        val password = layout.editText?.text?.trim().toString()
        val c2 = password.length >= 8 &&
                password.any { it.isLetter() } &&
                password.any { it.isDigit() }

        if (!c2) {
            layout.error = "Password must be at least 8 characters long and contain a number"
            layout.isErrorEnabled = true
        } else {
            layout.error = ""
            layout.isErrorEnabled = false
        }
        return c1 && c2
    }

    fun validateConformPassword(
        passwordLayout: TextInputLayout,
        coPasswordLayout: TextInputLayout
    ) : Boolean {
        val password = coPasswordLayout.editText?.text.toString().trim()
        val c1 = password.length >= 8 &&
                password.any { it.isLetter() } &&
                password.any { it.isDigit() }
        val c2 = passwordLayout.editText?.text.toString() == password

        if (!c1) {
            coPasswordLayout.error = "Password must be at least 8 characters long and contain a number"
            coPasswordLayout.isErrorEnabled = true
        }
        if (!c2) {
            coPasswordLayout.error = "Password not matched."
            coPasswordLayout.isErrorEnabled = true
        } else {
            coPasswordLayout.error = ""
            coPasswordLayout.isErrorEnabled = false
        }
        return c1 && c2
    }
}