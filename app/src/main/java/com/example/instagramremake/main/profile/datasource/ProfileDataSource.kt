package com.example.instagramremake.main.profile.datasource

import com.example.instagramremake.commom.presenter.Presenter

interface ProfileDataSource {
    fun findUser(uuid: String, presenter: Presenter)
    fun follow(uuid: String)
    fun unfollow(uuid: String)
}