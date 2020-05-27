package com.example.instagramremake.login.presentation

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import com.example.instagramremake.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), LoginView, TextWatcher {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this, R.color.colorAccent)
        }

        login_edit_text_email.addTextChangedListener(this)
        login_edit_text_password.addTextChangedListener(this)

        login_button_enter.setOnClickListener{
            login_button_enter.showProgress(true)

            Handler().postDelayed({
                login_button_enter.showProgress(false)

            }, 4000)

        }

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
        TODO("Not yet implemented")
    }

    override fun afterTextChanged(s: Editable?) {
        login_button_enter.isEnabled = s.toString().isNotEmpty()
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        TODO("Not yet implemented")
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        TODO("Not yet implemented")
    }

    fun showProgressBar(){
        login_button_enter.showProgress(true)
    }

    fun hideProgressBar(){
        login_button_enter.showProgress(false)
    }
}
