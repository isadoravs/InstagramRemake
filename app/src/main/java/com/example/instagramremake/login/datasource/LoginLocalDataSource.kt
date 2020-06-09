package com.example.instagramremake.login.datasource

import com.example.instagramremake.commom.model.Database
import com.example.instagramremake.commom.presenter.Presenter

class LoginLocalDataSource : LoginDataSource {
    private val database = Database()
    override fun onLogin(email: String, password: String, presenter: Presenter) {
        with(database) {
            login(email, password)
            onSuccessListener = { response ->
                presenter.onSuccess(response)
            }
            onFailureListener = { error ->
                error.message?.let { presenter.onError(it) }
            }
            onCompleteListener = { presenter.onComplete() }
        }

    }
}