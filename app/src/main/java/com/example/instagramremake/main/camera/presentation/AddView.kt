package com.example.instagramremake.main.camera.presentation

import android.net.Uri

interface AddView {
    fun onImageLoaded(uri: Uri)
    fun dispose()
}