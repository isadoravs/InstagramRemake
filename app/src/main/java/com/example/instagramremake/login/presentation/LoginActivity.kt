package com.example.instagramremake.login.presentation

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import com.example.instagramremake.R
import com.example.instagramremake.commom.model.Database
import com.example.instagramremake.login.datasource.LoginDataSource
import com.example.instagramremake.login.datasource.LoginLocalDataSource
import com.example.instagramremake.main.presentation.MainActivity
import com.example.instagramremake.register.presentation.RegisterActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), LoginView, View.OnClickListener {

    private lateinit var presenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setStatusBarDark()
        val user = Database.userAuth
        Log.e("aqui", user.toString())
        if(user != null) onUserLogged()

        login_edit_text_email.doOnTextChanged { s, _, _, _ -> onTextChanged(s) }
        login_edit_text_password.doOnTextChanged { s, _, _, _ -> onTextChanged(s) }

        val dataSource = LoginLocalDataSource()
        presenter = LoginPresenter(this, dataSource)
        login_button_enter.setOnClickListener(this)
        login_text_view_register.setOnClickListener(this)

    }

    override fun onFailureForm(emailError: String?, passwordError: String?) {
        emailError?.let {
            login_edit_text_email_input.error = it
            login_edit_text_email.background = ContextCompat.getDrawable(
                this@LoginActivity,
                R.drawable.edit_text_background_error
            )
        }
        passwordError?.let {
            login_edit_text_password_input.error = it
            login_edit_text_password.background = ContextCompat.getDrawable(
                this@LoginActivity,
                R.drawable.edit_text_background_error
            )
        }
    }

    override fun onUserLogged() {
        MainActivity.launch(this, MainActivity.LOGIN_ACTIVITY)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }


    private fun onTextChanged(s: CharSequence?) {
        login_button_enter.isEnabled = login_edit_text_email.text.toString()
            .isNotEmpty() && login_edit_text_password.text.toString().isNotEmpty()

        if (s.hashCode() == login_edit_text_email.text.hashCode()) {
            login_edit_text_email.background =
                ContextCompat.getDrawable(this, R.drawable.edit_text_background)
            login_edit_text_email_input.error = null
            login_edit_text_email_input.isErrorEnabled = false
        } else if (s.hashCode() == login_edit_text_password.text.hashCode()) {
            login_edit_text_password.background =
                ContextCompat.getDrawable(this, R.drawable.edit_text_background)
            login_edit_text_password_input.error = null
            login_edit_text_password_input.isErrorEnabled = false
        }
    }

    override fun showProgressBar() {
        login_button_enter.showProgress(true)
    }

    override fun hideProgressBar() {
        login_button_enter.showProgress(false)
    }

    override fun getContext(): Context {
        return baseContext
    }

    override fun setStatusBarDark() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this, R.color.colorAccent)
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.login_button_enter ->
                presenter.login(
                    login_edit_text_email.text.toString(),
                    login_edit_text_password.text.toString()
                )
            R.id.login_text_view_register -> {
                RegisterActivity.launch(this)
            }

        }
    }
}
