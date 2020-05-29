package com.example.instagramremake.login.presentation

import com.example.instagramremake.R
import com.example.instagramremake.commom.model.UserAuth
import com.example.instagramremake.commom.presenter.Presenter
import com.example.instagramremake.commom.util.Strings
import com.example.instagramremake.login.datasource.LoginDataSource


class LoginPresenter(val view: LoginView, val datasource: LoginDataSource) :  Presenter{
    fun login(email: String, password: String){
        if(!Strings.validEmail(email)){
            view.onFailureForm(view.getContext()?.getString(R.string.invalid_email), null)
        }
        view.showProgressBar()
        datasource.onLogin(email, password, this)
    }

    override fun onSuccess(response: Any) {
        view.onUserLogged()
    }

    override fun onError(message: String) {
        view.onFailureForm(null, message)
    }

    override fun onComplete() {
        view.hideProgressBar()
    }

}