package com.example.instagramremake.main.profile.datasource

import com.example.instagramremake.commom.model.Database
import com.example.instagramremake.commom.model.Post
import com.example.instagramremake.commom.model.User
import com.example.instagramremake.commom.model.UserProfile
import com.example.instagramremake.commom.presenter.Presenter

class ProfileLocalDataSource : ProfileDataSource {
    override fun findUser(uuid: String, presenter: Presenter) {
        val database = Database()

        Database.userAuth?.let {
            database.findUser(uuid)
            database.onSuccessListener = { response ->
                if (response is User) {
                    database.findPosts(uuid)
                    database.onSuccessListener = { posts ->
                        database.following(it.uuid, uuid)
                        database.onSuccessListener = {following ->
                            if (posts is List<*>) {
                                println("ON SUCCESS PROFILE LOCAL DATASOURCE")
                                presenter.onSuccess(UserProfile(response, posts as List<Post>, following as Boolean))
                                presenter.onComplete()
                            }
                        }
                    }
                }

            }
        }
    }

    override fun follow(uuid: String) {
        val database = Database()
        Database.userAuth?.uuid?.let { database.follow(it, uuid) }
    }

    override fun unfollow(uuid: String) {
        val database = Database()
        Database.userAuth?.uuid?.let { database.unfollow(it, uuid) }
    }

}