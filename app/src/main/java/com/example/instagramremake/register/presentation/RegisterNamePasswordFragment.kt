package com.example.instagramremake.register.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.example.instagramremake.R
import com.example.instagramremake.commom.view.AbstractFragment
import kotlinx.android.synthetic.main.fragment_register_email.view.*
import kotlinx.android.synthetic.main.fragment_register_name_password.*
import kotlinx.android.synthetic.main.fragment_register_name_password.view.*

class RegisterNamePasswordFragment : AbstractFragment<RegisterPresenter>(),
    RegisterView.NamePasswordView {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register_name_password, container, false)

        view.register_edit_text_name.doOnTextChanged { s, _, _, _ -> onTextChanged(s) }
        view.register_edit_text_password.doOnTextChanged { s, _, _, _ -> onTextChanged(s) }
        view.register_edit_text_password_confirm.doOnTextChanged { s, _, _, _ -> onTextChanged(s) }

        view.register_text_view_login.setOnClickListener {
            activity?.finish()
        }
        view.register_name_button_next.setOnClickListener {
            presenter?.name = view.register_edit_text_name.text.toString()
            presenter?.setPassword(
                view.register_edit_text_password.text.toString(),
                view.register_edit_text_password_confirm.text.toString()
            )
        }
        return view
    }

    override fun onFailureForm(nameError: String?, passwordError: String?) {
        if (nameError != null) {
            register_edit_text_name_input.error = nameError
            register_edit_text_name.background = context?.let {
                ContextCompat.getDrawable(
                    it,
                    R.drawable.edit_text_background_error
                )
            }
        }
        if (passwordError != null) {
            register_edit_text_password_input.error = passwordError
            register_edit_text_password.background = context?.let {
                ContextCompat.getDrawable(
                    it,
                    R.drawable.edit_text_background_error
                )
            }
        }
    }

    override fun showProgressBar() {
        register_name_button_next.showProgress(true)
    }

    override fun hideProgressBar() {
        register_name_button_next.showProgress(false)
    }

    private fun onTextChanged(s: CharSequence?) {
        register_name_button_next.isEnabled = register_edit_text_name.text.toString()
            .isNotEmpty() && register_edit_text_password.text.toString()
            .isNotEmpty() && register_edit_text_password_confirm.text.toString().isNotEmpty()

        register_edit_text_name.background =
            context?.let { ContextCompat.getDrawable(it, R.drawable.edit_text_background) }
        register_edit_text_name_input.error = null
        register_edit_text_name_input.isErrorEnabled = false

        register_edit_text_password.background =
            context?.let { ContextCompat.getDrawable(it, R.drawable.edit_text_background) }
        register_edit_text_password_input.error = null
        register_edit_text_password_input.isErrorEnabled = false

        register_edit_text_password_confirm.background =
            context?.let { ContextCompat.getDrawable(it, R.drawable.edit_text_background) }
        register_edit_text_name_input.error = null
        register_edit_text_password_confirm_input.isErrorEnabled = false
    }


    companion object {
        private lateinit var instance: RegisterNamePasswordFragment
        fun newInstance(presenter: RegisterPresenter): RegisterNamePasswordFragment {
            val fragment = RegisterNamePasswordFragment()
            fragment.presenter = presenter
            presenter.namePasswordView = fragment
            return fragment
        }
    }
}