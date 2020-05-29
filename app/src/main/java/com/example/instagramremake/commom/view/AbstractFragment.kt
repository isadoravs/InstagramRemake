package com.example.instagramremake.commom.view

import android.content.Context
import android.os.Build
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.instagramremake.R

abstract class AbstractFragment<P> : Fragment(), View {

    protected var presenter: P? = null

    override fun getContext(): Context? {
        return super.getContext()
    }

    override fun showProgressBar() {
    }

    override fun hideProgressBar() {
    }

    override fun setStatusBarDark() {
        context?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//                window.statusBarColor = ContextCompat.getColor(it, R.color.colorAccent)
            }
        }
    }

}