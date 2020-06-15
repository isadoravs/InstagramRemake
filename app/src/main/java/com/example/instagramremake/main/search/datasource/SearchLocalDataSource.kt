package com.example.instagramremake.main.search.datasource

import com.example.instagramremake.commom.model.Database
import com.example.instagramremake.commom.model.User
import com.example.instagramremake.commom.presenter.Presenter

class SearchLocalDataSource : SearchDataSource{
    override fun findUsers(query: String, presenter: Presenter) {
        val database = Database()
        Database.userAuth?.let{
            database.findUsers(it.uuid, query)
            database.onSuccessListener = {response ->
                    presenter.onSuccess(response)
            }
            database.onFailureListener = { error ->
                error.message?.let { msg -> presenter.onError(msg) }
            }
            database.onCompleteListener = {
                presenter.onComplete()
            }
        }

    }

}