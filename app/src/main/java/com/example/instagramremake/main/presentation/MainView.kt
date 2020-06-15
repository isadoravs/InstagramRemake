package com.example.instagramremake.main.presentation

import android.net.Uri
import com.example.instagramremake.commom.model.Feed
import com.example.instagramremake.commom.model.Post
import com.example.instagramremake.commom.model.User
import com.example.instagramremake.commom.view.View

interface MainView : View {
    fun scrollToolbarEnabled(enabled: Boolean)
    fun showProfile(user: String)
    fun disposeProfileDetail()
//    fun logout()

    interface ProfileView: View {
        fun showPhoto(photo: Uri)
        fun showData(
            name: String,
            following: String,
            followers: String,
            posts: String,
            editProfile: Boolean,
            follow: Boolean
        )
        fun showPosts(posts: List<Post>)
    }

    interface HomeView: View {
        fun showFeed(response: List<Feed>)
    }

    interface SearchView {
        fun showUsers(users: List<User>)
    }
}