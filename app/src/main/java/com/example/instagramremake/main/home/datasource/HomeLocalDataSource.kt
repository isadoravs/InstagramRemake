package com.example.instagramremake.main.home.datasource

import android.util.Log
import com.example.instagramremake.commom.model.Database
import com.example.instagramremake.commom.presenter.Presenter

class HomeLocalDataSource : HomeDataSource {
    private val database = Database()
    override fun findFeed(presenter: Presenter) {
        Database.userAuth?.let { userAuth ->
            with(database) {
                findFeed(userAuth.uuid)
                Log.e("aqui", "homelocaldatasouce findfeed")
                onSuccessListener = { response -> presenter.onSuccess(response) }
                onFailureListener = { error ->
                    error.message?.let { presenter.onError(it) }
                }
                onCompleteListener = { presenter.onComplete() }
            }
        }
    }

}