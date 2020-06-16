package com.example.instagramremake.main.camera.datasource

import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import com.example.instagramremake.commom.presenter.Presenter

class GalleryLocalDataSource: GalleryDataSource {

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun findPictures(context: Context, presenter: Presenter) {
        val images: MutableList<String> = ArrayList()
        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        val projection = arrayOf(MediaStore.Images.Media.DATA,
            MediaStore.MediaColumns.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.DATE_ADDED)

        val cursor = context.contentResolver.query(uri, projection, "bucket_display_name LIKE 'Pixelgram'", null,
            MediaStore.Images.Media.DATE_ADDED + " DESC");

        if (cursor != null) {
            val columnIndexData = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            val columnIndexFolder = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
            val columnIndexDataAdded = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED);
            while (cursor.moveToNext() && cursor.position < 30) {
                val absolutePathImage = cursor.getString(columnIndexData);
                val folder = cursor.getString(columnIndexFolder);
                val dateAdded = cursor.getString(columnIndexDataAdded);

                images.add(absolutePathImage);
            }
            cursor.close()
        }

        if (images.isNotEmpty())
            presenter.onSuccess(images);
    }

}