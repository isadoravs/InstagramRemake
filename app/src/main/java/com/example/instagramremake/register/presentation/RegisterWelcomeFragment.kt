package com.example.instagramremake.register.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.instagramremake.R
import com.example.instagramremake.commom.view.AbstractFragment
import kotlinx.android.synthetic.main.fragment_register_email.view.*
import kotlinx.android.synthetic.main.fragment_register_email.view.register_button_next
import kotlinx.android.synthetic.main.fragment_register_welcome.view.*

class RegisterWelcomeFragment : AbstractFragment<RegisterPresenter>(), RegisterView.WelcomeView {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register_welcome, container, false)
        view.register_button_next.isEnabled = true
        view.register_button_next.setOnClickListener {
            presenter?.showPhotoView()
        }
        view.register_text_view_welcome.text = getString(R.string.welcome_to_instagram,
            presenter?.name
        )
        return view
    }

    companion object {
        private lateinit var instance: RegisterWelcomeFragment
        fun newInstance(presenter: RegisterPresenter): RegisterWelcomeFragment {
            val fragment = RegisterWelcomeFragment()
            fragment.presenter = presenter
            presenter.welcomeView = fragment
            return fragment
        }
    }
}