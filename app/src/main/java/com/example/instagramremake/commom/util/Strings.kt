package com.example.instagramremake.commom.util

class Strings {
    companion object {
        fun validEmail(email: String): Boolean {
            return if (email.isEmpty()) {
                false;
            } else {
                android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
            }
        }
    }
}