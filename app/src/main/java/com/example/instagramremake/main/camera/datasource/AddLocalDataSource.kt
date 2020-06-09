package com.example.instagramremake.main.camera.datasource

import android.net.Uri
import com.example.instagramremake.commom.model.Database
import com.example.instagramremake.commom.presenter.Presenter

class AddLocalDataSource: AddDataSource {
    private val database = Database()
    override fun savePost(uri: Uri, caption: String, presenter: Presenter) {
        Database.userAuth?.uuid?.let { userAuth ->
            with(database){
                createPost(userAuth, uri, caption)
                onSuccessListener = { response -> presenter.onSuccess(response) }
                onFailureListener = { error ->
                    error.message?.let { presenter.onError(it) }
                }
                onCompleteListener = { presenter.onComplete() }
            }

        }

    }
}