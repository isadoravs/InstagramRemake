package com.example.instagramremake.main.home.datasource

import com.example.instagramremake.commom.model.Feed

import com.example.instagramremake.commom.presenter.Presenter




interface HomeDataSource {
    fun findFeed(presenter: Presenter)
}