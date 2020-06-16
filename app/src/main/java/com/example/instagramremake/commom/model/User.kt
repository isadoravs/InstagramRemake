package com.example.instagramremake.commom.model

import android.net.Uri

data class User(
    var uuid: String = "",
    var email: String = "",
    var name: String = "",
    var uri: Uri? = null,
    var photoUrl: String? = null,
    var following: Int = 0,
    var followers: Int = 0,
    var posts: Int = 0
)