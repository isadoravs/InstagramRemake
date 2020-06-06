package com.example.instagramremake.register.datasource

import android.net.Uri
import com.example.instagramremake.commom.presenter.Presenter

interface RegisterDataSource {
    fun createUser(name: String, email: String, password: String, presenter: Presenter)
    fun addPhoto(uri: Uri, presenter: Presenter)
}