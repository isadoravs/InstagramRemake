package com.example.instagramremake.commom.model

import android.net.Uri
import androidx.core.net.toUri

data class Post(
    val uuid: String = "",
    val uri: Uri? = "".toUri(),
    val caption: String = "",
    val timestamp: Long = 0,
    var photoUrl: String = ""
)