package com.example.instagramremake.register.presentation

import android.content.Context
import android.content.res.Resources
import com.example.instagramremake.R
import com.example.instagramremake.commom.util.Strings

class RegisterPresenter {

    lateinit var registerView: RegisterView
    lateinit var emailView: RegisterView.EmailView
    lateinit var namePasswordView: RegisterView.NamePasswordView

    lateinit var name: String
    private lateinit var password: String
    var email: String = ""
        set(value) {
            if (!Strings.validEmail(value)) {
                emailView.onFailureForm(emailView.getContext()!!.getString(R.string.invalid_email))
                return
            }
            field = email
            registerView.showNextView(RegisterSteps.NAME_PASSWORD)
        }
    fun setPassword(password: String, confirmPassword: String){
        if(password != confirmPassword){
            namePasswordView.onFailureForm(null, namePasswordView.getContext()!!.getString(R.string.password_not_equal))
            return
        }
        this.password = password
    }
}