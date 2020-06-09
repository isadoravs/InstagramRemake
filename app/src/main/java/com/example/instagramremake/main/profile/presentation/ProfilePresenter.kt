package com.example.instagramremake.main.profile.presentation

import android.util.Log
import com.example.instagramremake.commom.model.UserProfile
import com.example.instagramremake.commom.presenter.Presenter
import com.example.instagramremake.main.presentation.MainView
import com.example.instagramremake.main.profile.datasource.ProfileDataSource

class ProfilePresenter(private val dataSource: ProfileDataSource) : Presenter{
    lateinit var view: MainView.ProfileView

    fun findUser(){
        view.showProgressBar()
        dataSource.findUser(this)
    }

    override fun onSuccess(response: Any) {
        Log.e("aqui profile presenter", response.javaClass.toString())
        response as UserProfile
        with(response){
            view.showData(user.name, user.following.toString(), user.followers.toString(), user.posts.toString())
            view.showPosts(posts)
            user.uri?.let { view.showPhoto(it) }
        }
    }

    override fun onError(message: String) {
    }

    override fun onComplete() {
        view.hideProgressBar()
    }
}