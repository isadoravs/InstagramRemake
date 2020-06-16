package com.example.instagramremake.register.presentation

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.instagramremake.R
import com.example.instagramremake.commom.component.MediaHelper
import com.example.instagramremake.main.presentation.MainActivity
import com.example.instagramremake.register.datasource.RegisterFireDataSource
import com.example.instagramremake.register.datasource.RegisterLocalDataSource
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity(), RegisterView, MediaHelper.OnImageCroppedListener {

    private lateinit var presenter: RegisterPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this, R.color.colorAccent)
        }
        val dataSource = RegisterFireDataSource()
        presenter = RegisterPresenter(dataSource)
        presenter.registerView = this

        button_crop.setOnClickListener {
            cropViewEnabled(false)
            MediaHelper.cropImage(this, register_crop_image_view)
        }

        with(MediaHelper) {
            cropView(register_crop_image_view)
            listener = this@RegisterActivity
        }

        showNextView(RegisterSteps.EMAIL)

    }


    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, RegisterActivity::class.java)
            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    override fun showNextView(step: RegisterSteps) {
        val fragment: Fragment
        val linearLayout = register_scrollview.layoutParams

        fragment = when (step) {
            RegisterSteps.EMAIL -> {
                RegisterEmailFragment.newInstance(presenter)
            }
            RegisterSteps.NAME_PASSWORD -> {
                RegisterNamePasswordFragment.newInstance(presenter)
            }
            RegisterSteps.WELCOME -> RegisterWelcomeFragment.newInstance(presenter)
            RegisterSteps.PHOTO -> {
                val params = register_scrollview.layoutParams as FrameLayout.LayoutParams
                params.gravity = Gravity.TOP
                register_scrollview.layoutParams = params
                RegisterPhotoFragment.newInstance(presenter)
            }

        }

        val transaction = supportFragmentManager.beginTransaction()

        if (supportFragmentManager.findFragmentById(R.id.register_fragment) == null) {
            transaction.add(R.id.register_fragment, fragment, step.name)
        } else {
            transaction.replace(R.id.register_fragment, fragment, step.name)
            transaction.addToBackStack(step.name)
        }
        transaction.commit()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        cropViewEnabled(true)
        MediaHelper.onActivityResult(this, requestCode, resultCode, data)
    }

    private fun cropViewEnabled(enabled: Boolean) {
        register_crop_image_view.visibility = if (enabled) View.VISIBLE else View.GONE
        register_scrollview.visibility = if (enabled) View.GONE else View.VISIBLE
        button_crop.visibility = if (enabled) View.VISIBLE else View.GONE
        register_root_container.setBackgroundColor(
            if (enabled) ContextCompat.getColor(this, android.R.color.black)
            else ContextCompat.getColor(this, android.R.color.white)
        )
    }

    override fun onUserCreated() {
        MainActivity.launch(this, MainActivity.REGISTER_ACTIVITY)
    }

    override fun showCamera() {
        MediaHelper.chooseCamera(this@RegisterActivity)
    }

    override fun showGallery() {
        MediaHelper.chooseGallery(this@RegisterActivity)
    }

    override fun onImageCropped(uri: Uri) {
        presenter.uri = uri
    }

    override fun onImagePicked(mCropImageUri: Uri) {
        register_crop_image_view.setImageUriAsync(mCropImageUri)
    }
}
