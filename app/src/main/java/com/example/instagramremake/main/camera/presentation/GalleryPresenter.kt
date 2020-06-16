package com.example.instagramremake.main.camera.presentation

import android.content.Context
import android.net.Uri
import com.example.instagramremake.commom.presenter.Presenter
import com.example.instagramremake.main.camera.datasource.GalleryDataSource
import java.io.File

class GalleryPresenter(private val dataSource: GalleryDataSource): Presenter {
    lateinit var view: GalleryView


    fun findPictures(context: Context){
        view.showProgressBar()
        dataSource.findPictures(context, this)
    }

    override fun onSuccess(response: Any) {
        if(response is List<*>){
            val uris = ArrayList<Uri>()
            response.forEach{
                it as String
                val uri = Uri.parse(it)
                uris.add(uri)
            }
            view.onPicturesLoaded(uris)
        }

    }

    override fun onError(message: String) {
    }

    override fun onComplete() {
        view.hideProgressBar()
    }
}