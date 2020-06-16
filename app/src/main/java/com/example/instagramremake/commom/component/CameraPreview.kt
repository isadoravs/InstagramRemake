package com.example.instagramremake.commom.component

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.Camera
import android.view.SurfaceHolder
import android.view.SurfaceView
import java.io.IOException

class CameraPreview(context: Context, private val camera: Camera): SurfaceView(context), SurfaceHolder.Callback {
    init {
        holder.addCallback(this)
    }
    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        if (holder!!.surface == null) return
        try {
            camera.stopPreview()
            camera.setPreviewDisplay(holder)
            camera.setDisplayOrientation(90)

            val supportedPreviewSizes = camera.parameters.supportedPreviewSizes
            val w = supportedPreviewSizes.first().width
            val h = supportedPreviewSizes.first().height

            camera.parameters.setPreviewSize(w, h)
            camera.parameters.setPictureSize(w, h)
            camera.startPreview()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        try{
            camera.setPreviewDisplay(holder)
            camera.startPreview()
        } catch (e: IOException){
            e.printStackTrace()
        }

    }

}