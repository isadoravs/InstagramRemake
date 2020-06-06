package com.example.instagramremake.register.presentation

import android.content.Context
import android.net.Uri
import com.example.instagramremake.commom.view.View

interface RegisterView {
    fun showNextView(step: RegisterSteps)

    fun onUserCreated()

    fun showCamera()

    fun showGallery()

    interface EmailView {
        fun getContext(): Context?
        fun onFailureForm(emailError: String)
    }

    interface NamePasswordView : View {
        fun onFailureForm(nameError: String?, passwordError: String?)
        fun onFailureCreateUser(message: String)
    }

    interface WelcomeView {}

    interface PhotoView: View {
        fun onImageCropped(uri: Uri)
    }
}
