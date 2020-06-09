package com.example.instagramremake.main.home.presentation

import android.util.Log
import com.example.instagramremake.commom.model.Feed
import com.example.instagramremake.commom.presenter.Presenter
import com.example.instagramremake.main.home.datasource.HomeDataSource
import com.example.instagramremake.main.presentation.MainView

class HomePresenter(private val dataSource: HomeDataSource) : Presenter{
    lateinit var view: MainView.HomeView

    fun findFeed(){
        view.showProgressBar()
        dataSource.findFeed(this)
    }

    override fun onSuccess(response: Any) {
        if(response is List<*>) {
            response as List<Feed>
            view.showFeed(response)
        }
    }

    override fun onError(message: String) {
    }

    override fun onComplete() {
        view.hideProgressBar()
    }
}