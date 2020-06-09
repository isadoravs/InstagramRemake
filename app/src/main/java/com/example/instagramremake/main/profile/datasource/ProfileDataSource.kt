package com.example.instagramremake.main.profile.datasource

import com.example.instagramremake.commom.presenter.Presenter

interface ProfileDataSource {
    fun findUser(presenter: Presenter)
}