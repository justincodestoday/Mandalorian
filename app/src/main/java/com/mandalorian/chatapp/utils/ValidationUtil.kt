package com.mandalorian.chatapp.utils

import android.util.Patterns.EMAIL_ADDRESS

object ValidationUtil {
    fun validateEmail(email: String): Boolean {
//        if (EMAIL_ADDRESS.matcher(email).matches()) {
//            return true
//        }
        val emailReg = Regex(
            "[a-z\\d]{1,256}(\\.[a-z\\d]){0,25}" +
                    "@" +
                    "[a-z\\d]+[a-z\\d]{0,64}" +
                    "(\\.[a-z\\d][a-z\\d]{0,25})+",
            RegexOption.IGNORE_CASE
        )
        if (emailReg.matches(email)) return true

        return false
    }

    fun validateUsername(username: String): Boolean {
        if (Regex("[a-z\\d]{5,20}", RegexOption.IGNORE_CASE).matches(username)) return true

        return false
    }
}