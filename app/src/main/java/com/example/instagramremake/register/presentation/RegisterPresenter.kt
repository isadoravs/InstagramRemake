package com.example.instagramremake.register.presentation

import android.net.Uri
import com.example.instagramremake.R
import com.example.instagramremake.commom.presenter.Presenter
import com.example.instagramremake.commom.util.Strings
import com.example.instagramremake.register.datasource.RegisterDataSource
import com.example.instagramremake.register.datasource.RegisterLocalDataSource
import java.util.*

class RegisterPresenter(private val dataSource: RegisterDataSource) : Presenter {

    lateinit var registerView: RegisterView
    lateinit var emailView: RegisterView.EmailView
    lateinit var namePasswordView: RegisterView.NamePasswordView
    lateinit var welcomeView: RegisterView.WelcomeView
    lateinit var photoView: RegisterView.PhotoView

    lateinit var name: String

    var uri: Uri? = null
        set(value){
            field = value
            photoView.onImageCropped(value!!)
            photoView.showProgressBar()

            dataSource.addPhoto(value, UpdatePhotoCallBack())
        }

    var email: String = ""
        set(value) {
            if (!Strings.validEmail(value)) {
                emailView.onFailureForm(emailView.getContext()!!.getString(R.string.invalid_email))
                return
            }
            field = value
            registerView.showNextView(RegisterSteps.NAME_PASSWORD)
        }

    fun setPasswordName(name: String, password: String, confirmPassword: String){
        if(password != confirmPassword){
            namePasswordView.onFailureForm(null, namePasswordView.getContext()!!.getString(R.string.password_not_equal))
            return
        }

        this.name = name

        namePasswordView.showProgressBar()
        dataSource.createUser(name.toLowerCase(Locale.ROOT), this.email, password, this)
    }


    fun showPhotoView(){
        registerView.showNextView(RegisterSteps.PHOTO)
    }

    fun jumpRegistration(){
        registerView.onUserCreated()
    }

    fun showCamera(){
        registerView.showCamera()
    }

    fun showGallery(){
        registerView.showGallery()
    }

    override fun onSuccess(response: Any) {
        registerView.showNextView(RegisterSteps.WELCOME)
    }

    override fun onError(message: String) {
        namePasswordView.onFailureCreateUser(message)
    }

    override fun onComplete() {
        namePasswordView.hideProgressBar()
    }

    inner class UpdatePhotoCallBack: Presenter {
        override fun onSuccess(response: Any) {
            registerView.onUserCreated()
        }

        override fun onError(message: String) {
           }

        override fun onComplete() {
        }

    }
}