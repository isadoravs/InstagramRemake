package com.example.instagramremake.register.datasource

import android.net.Uri
import com.example.instagramremake.commom.model.Database
import com.example.instagramremake.commom.presenter.Presenter

class RegisterLocalDataSource: RegisterDataSource {
    override fun createUser(name: String, email: String, password: String, presenter: Presenter) {
        with(Database) {
            createUser(name, email, password)
            onSuccessListener = { response ->
                presenter.onSuccess(response)
            }
            onFailureListener = { error ->
                error.message?.let { presenter.onError(it) }
            }
            onCompleteListener = { presenter.onComplete() }
        }
    }

    override fun addPhoto(uri: Uri, presenter: Presenter) {
        with(Database){
            userAuth?.let{
                addPhoto(it.uuid, uri)
                onSuccessListener = { response ->
                    presenter.onSuccess(response)
                }
            }

        }
    }
}