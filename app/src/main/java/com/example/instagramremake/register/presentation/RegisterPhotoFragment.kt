package com.example.instagramremake.register.presentation

import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import com.example.instagramremake.R
import com.example.instagramremake.commom.view.AbstractFragment
import com.example.instagramremake.commom.component.CustomDialog
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.view.*
import kotlinx.android.synthetic.main.fragment_register_email.view.register_button_next
import kotlinx.android.synthetic.main.fragment_register_photo.*

class RegisterPhotoFragment : AbstractFragment<RegisterPresenter>(), RegisterView.PhotoView {
    private lateinit var imageView: CropImageView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register_photo, container, false)
        view.register_button_next.isEnabled = true
        view.register_button_next.setOnClickListener {
            context?.let { context ->
                val customDialog = CustomDialog.Builder(context)
                    .setTitle(R.string.define_photo_profile)
                    .addButton(View.OnClickListener {
                        when (it.id) {
                            R.string.take_picture -> presenter?.showCamera()
                            R.string.search_gallery -> presenter?.showGallery()
                        }
                    }, R.string.take_picture, R.string.search_gallery).build()
                customDialog.show()
            }
        }
//        view.register_button_jump.setOnClickListener{ presenter?.jumpRegistration() }
        return view
    }


    @RequiresApi(Build.VERSION_CODES.P)
    override fun onImageCropped(uri: Uri) {
        context?.let{
            val bitmap =
                ImageDecoder.decodeBitmap(ImageDecoder.createSource(it.contentResolver, uri))
                register_camera_icon.setImageBitmap(bitmap)
        }
    }

    override fun showProgressBar() {
        register_button_next.showProgress(true)
    }

    override fun hideProgressBar() {
        register_button_next.showProgress(false)
    }

    companion object {
        private lateinit var instance: RegisterPhotoFragment
        fun newInstance(presenter: RegisterPresenter): RegisterPhotoFragment {
            val fragment = RegisterPhotoFragment()
            fragment.presenter = presenter
            presenter.photoView = fragment
            return fragment
        }
    }

}