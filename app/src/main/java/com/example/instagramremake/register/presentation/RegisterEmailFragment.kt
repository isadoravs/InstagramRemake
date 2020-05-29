package com.example.instagramremake.register.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import com.example.instagramremake.R
import com.example.instagramremake.commom.view.AbstractFragment
import kotlinx.android.synthetic.main.fragment_register_email.*
import kotlinx.android.synthetic.main.fragment_register_email.view.*

class RegisterEmailFragment : AbstractFragment<RegisterPresenter>(), RegisterView.EmailView {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register_email, container, false)

        view.register_edit_text_email.doOnTextChanged { s, _, _, _ -> onTextChanged(s) }
        view.register_text_view_email_login.setOnClickListener {
            activity?.finish()
        }
        view.register_button_next.setOnClickListener {
            presenter?.email = view.register_edit_text_email.text.toString()
        }
        return view
    }

    override fun showProgressBar() {
        register_button_next.showProgress(true)
    }

    override fun hideProgressBar() {
        register_button_next.showProgress(false)
    }

    private fun onTextChanged(s: CharSequence?) {
        register_button_next.isEnabled = register_edit_text_email.text.toString().isNotEmpty()
        register_edit_text_email.background =
            context?.let { ContextCompat.getDrawable(it, R.drawable.edit_text_background) }
        register_edit_text_email_input.error = null
        register_edit_text_email_input.isErrorEnabled = false
    }


    override fun onFailureForm(emailError: String) {
        register_edit_text_email_input.error = emailError
        register_edit_text_email.background = context?.let { ContextCompat.getDrawable(it, R.drawable.edit_text_background_error) }
    }

    companion object {
        private lateinit var instance: RegisterEmailFragment
        fun newInstance(presenter: RegisterPresenter): RegisterEmailFragment {
            val fragment = RegisterEmailFragment()
            fragment.presenter = presenter
            presenter.emailView = fragment
            return fragment
        }
    }

}