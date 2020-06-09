package com.example.instagramremake.main.camera.datasource

import android.content.Context
import com.example.instagramremake.commom.presenter.Presenter

interface GalleryDataSource {
    fun findPictures(context: Context, presenter: Presenter)
}