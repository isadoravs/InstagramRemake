package com.example.instagramremake.main.profile.presentation

import com.example.instagramremake.commom.model.Database
import com.example.instagramremake.commom.model.UserProfile
import com.example.instagramremake.commom.presenter.Presenter
import com.example.instagramremake.main.presentation.MainView
import com.example.instagramremake.main.profile.datasource.ProfileDataSource
import com.google.firebase.auth.FirebaseAuth

class ProfilePresenter(private val dataSource: ProfileDataSource, val uuid: String? = FirebaseAuth.getInstance().uid) : Presenter{
    lateinit var view: MainView.ProfileView

    fun findUser() {
        view.showProgressBar()
        if (uuid != null) {
            dataSource.findUser(uuid, this)
        }
    }

    fun follow(follow: Boolean){
        uuid?.let{
            if(follow) dataSource.follow(it)
            else dataSource.unfollow(it)
        }
    }

    override fun onSuccess(response: Any) {
        response as UserProfile
        with(response){
            val editProfile = user.uuid == FirebaseAuth.getInstance().uid
            view.showData(user.name, user.following.toString(), user.followers.toString(), user.posts.toString(), editProfile, following)
            view.showPosts(posts)
            user.photoUrl?.let { view.showPhoto(it) }
        }
    }

    override fun onError(message: String) {
    }

    override fun onComplete() {
        view.hideProgressBar()
    }
}