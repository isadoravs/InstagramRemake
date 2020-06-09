package com.example.instagramremake.main.camera.presentation

import android.net.Uri
import com.example.instagramremake.commom.view.View

interface GalleryView: View {
    fun onPicturesLoaded(uris: List<Uri>)
}