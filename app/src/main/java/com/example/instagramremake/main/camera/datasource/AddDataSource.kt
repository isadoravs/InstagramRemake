package com.example.instagramremake.main.camera.datasource

import android.net.Uri
import com.example.instagramremake.commom.presenter.Presenter

interface AddDataSource {
    fun savePost(uri: Uri, caption: String, presenter: Presenter)
}