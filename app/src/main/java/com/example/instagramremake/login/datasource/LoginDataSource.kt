package com.example.instagramremake.login.datasource

import com.example.instagramremake.commom.presenter.Presenter

interface LoginDataSource {
    fun onLogin(email: String, password: String, presenter: Presenter)
}