package com.application.studyroom.auth.presentation.validation

object PasswordUtils {
    fun isValidPassword(password: String): String? {
        if (password.length < 8) {
            return "Password must be at least 8 characters long."
        }
        if (!password.any { it.isUpperCase() }) {
            return "Password must contain at least one uppercase letter."
        }
        if (!password.any { it.isLowerCase() }) {
            return "Password must contain at least one lowercase letter."
        }
        if (!password.any { it.isDigit() }) {
            return "Password must contain at least one digit."
        }
        if (!password.any { "!@#\$%^&*()-_=+[]{}|;:'\",.<>?/`~\\".contains(it) }) {
            return "Password must contain at least one special character."
        }
        return null
    }
}