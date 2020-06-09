package com.example.instagramremake.main.camera.presentation

import android.net.Uri
import com.example.instagramremake.commom.presenter.Presenter
import com.example.instagramremake.main.camera.datasource.AddDataSource

class AddPresenter(private val view: AddCaptionView, private val dataSource: AddDataSource): Presenter {

    fun createPost(uri: Uri, caption: String){
        view.showProgressBar()
        dataSource.savePost(uri, caption, this)
    }

    override fun onSuccess(response: Any) {
        view.postSaved()
    }

    override fun onError(message: String) {
        TODO("Not yet implemented")
    }

    override fun onComplete() {
        view.hideProgressBar()
    }
}