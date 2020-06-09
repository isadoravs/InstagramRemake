package com.example.instagramremake.commom.model

import android.net.Uri

data class User(
    val uuid: String,
    val email: String,
    val name: String,
    var uri: Uri? = null,
    val following: Int = 0,
    val followers: Int = 0,
    val posts: Int = 0
)