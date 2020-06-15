package com.example.instagramremake.main.search.datasource

import com.example.instagramremake.commom.presenter.Presenter

interface SearchDataSource {
    fun findUsers(query: String, presenter: Presenter)
}