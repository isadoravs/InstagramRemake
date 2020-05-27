package com.example.instagramremake.login.presentation

interface LoginView {
    fun onFailureForm(emailError: String?, passwordError: String?)
    fun onUserLogged()
}