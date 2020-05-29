package com.example.instagramremake.login.datasource

import com.example.instagramremake.commom.model.Database
import com.example.instagramremake.commom.model.UserAuth
import com.example.instagramremake.commom.presenter.Presenter
import java.lang.Exception

class LoginLocalDataSource : LoginDataSource {
    override fun onLogin(email: String, password: String, presenter: Presenter) {
        with(Database) {
            this.login(email, password)
            this.onSuccessListener = { response ->
                presenter.onSuccess(response)
            }
            this.onFailureListener = { error ->
                error.message?.let { presenter.onError(it) }
            }
            this.onCompleteListener = { presenter.onComplete() }
        }

    }
}