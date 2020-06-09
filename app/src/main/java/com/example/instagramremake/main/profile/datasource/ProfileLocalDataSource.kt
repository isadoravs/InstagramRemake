package com.example.instagramremake.main.profile.datasource

import android.util.Log
import com.example.instagramremake.commom.model.Database
import com.example.instagramremake.commom.model.Post
import com.example.instagramremake.commom.model.User
import com.example.instagramremake.commom.model.UserProfile
import com.example.instagramremake.commom.presenter.Presenter

class ProfileLocalDataSource : ProfileDataSource {
    private val database = Database()
    override fun findUser(presenter: Presenter) {
        Database.userAuth?.let {
            with(database) {
                findUser(it.uuid)
                onSuccessListener = { user ->
                    if (user is User) {
                        user as User
                        Log.e("aqui", "profile localdatasource finduser")
                        findPosts(user.uuid)
                        onSuccessListener = { posts ->
                            if (posts is List<*>) {
                                Log.e("aqui", "profile localdatasource findposts")
                                Log.e("aqui", posts.javaClass.toString())

                                presenter.onSuccess(UserProfile(user, posts as List<Post>))
                                presenter.onComplete()
                            }
                        }
                    }
                }
            }
        }
    }

}