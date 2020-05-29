package com.example.instagramremake.register.presentation

import android.content.Context
import com.example.instagramremake.commom.view.View

interface RegisterView {
    fun showNextView(step: RegisterSteps)

    interface EmailView {
        fun getContext(): Context?

        fun onFailureForm(emailError: String)
    }

    interface NamePasswordView {
        fun getContext(): Context?

        fun onFailureForm(nameError: String?, passwordError: String?)
    }
}
