package com.example.instagramremake.main.search.presentation

import com.example.instagramremake.commom.model.User
import com.example.instagramremake.commom.presenter.Presenter
import com.example.instagramremake.main.presentation.MainView
import com.example.instagramremake.main.search.datasource.SearchDataSource

class SearchPresenter(private val dataSource: SearchDataSource): Presenter {
    lateinit var view: MainView.SearchView
    override fun onSuccess(response: Any) {
        if(response is List<*>){
            response as List<User>
            view.showUsers(response)
        }
    }

    fun findUsers(newText: String) {
        dataSource.findUsers(newText, this)
    }

    override fun onError(message: String) {
    }

    override fun onComplete() {
    }

}