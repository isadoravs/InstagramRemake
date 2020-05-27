package com.example.instagramremake.register.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.instagramremake.R
import com.example.instagramremake.commom.view.CustomDialog

class RegisterPhotoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register_photo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        context?.let { context ->
            val customDialog = CustomDialog.Builder(context)
                .setTitle(R.string.define_photo_profile)
                .addButton(View.OnClickListener {
                    when (it.id) {
                        R.string.take_picture -> println("take pic")
                        R.string.search_gallery -> println("search gallery")
                    }
            }, R.string.take_picture, R.string.search_gallery).build()
            customDialog.show()

        }
    }
}