package com.example.instagramremake.login.presentation

import com.example.instagramremake.commom.view.View

interface LoginView: View {
    fun onFailureForm(emailError: String?, passwordError: String?)
    fun onUserLogged()
}