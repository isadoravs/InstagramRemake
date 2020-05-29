package com.example.instagramremake.register.presentation

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.instagramremake.R
import kotlinx.android.synthetic.main.fragment_register_email.*

class RegisterActivity : AppCompatActivity(), RegisterView {

    private lateinit var presenter: RegisterPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this, R.color.colorAccent)
        }

        presenter = RegisterPresenter()
        presenter.registerView = this

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

        fragment = when (step) {
            RegisterSteps.EMAIL -> {
                RegisterEmailFragment.newInstance(presenter)
            }
            RegisterSteps.NAME_PASSWORD -> {
                RegisterNamePasswordFragment.newInstance(presenter)
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
}
