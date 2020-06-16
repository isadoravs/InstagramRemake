package com.example.instagramremake.main.camera.presentation

import android.hardware.Camera
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.instagramremake.R
import com.example.instagramremake.commom.component.CameraPreview
import com.example.instagramremake.commom.component.MediaHelper
import kotlinx.android.synthetic.main.fragment_main_camera.view.*

class CameraFragment : Fragment() {

    private var camera: Camera? = null
    private lateinit var addView: AddView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main_camera, container, false)
        context?.let {
            if(MediaHelper.checkCameraHardware(it)) {
                camera = MediaHelper.getCameraInstance(this)
                camera?.let {camera ->
                    val cameraPreview = CameraPreview(it, camera)
                    view.camera_surface.addView(cameraPreview)
                }
            }
        }
        view.camera_image_view_picture.setOnClickListener {
            view.camera_progress.visibility = View.VISIBLE
            view.camera_image_view_picture.visibility = View.GONE
            camera?.takePicture(null, null,
                Camera.PictureCallback { data, _ ->
                    view.camera_progress.visibility = View.GONE
                    view.camera_image_view_picture.visibility = View.VISIBLE
                    context?.let { it ->
                        var uri = MediaHelper.saveCameraFile(it, data)
                        if(uri != null) addView.onImageLoaded(uri)
                    }
                })

        }
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        camera?.release()
    }

    companion object {
        fun newInstance(addView: AddView): CameraFragment {
            val fragment = CameraFragment()
            fragment.addView = addView
            return fragment
        }
    }


}
