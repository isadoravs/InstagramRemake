package com.example.instagramremake.commom.component

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PorterDuff
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.isVisible
import com.example.instagramremake.R

class ProgressButton@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null,
                                              defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

    init{
        if (attrs != null) {
            setup(context, attrs)
        }
    }

    private lateinit var text: String
    private lateinit var progressbar: ProgressBar
    private lateinit var button: AppCompatButton


    @SuppressLint("Recycle")
    private fun setup(context: Context, attrs: AttributeSet){
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.button_loading, this, true)

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.ProgressButton, 0, 0)
        text = attributes.getString(R.styleable.ProgressButton_text).toString()

        button = getChildAt(0) as AppCompatButton
        button.id = R.id.login_button_enter
        button.text = text
        button.isEnabled = false

        progressbar = getChildAt(1) as ProgressBar
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            val wrap = DrawableCompat.wrap(progressbar.indeterminateDrawable)
            DrawableCompat.setTint(wrap, ContextCompat.getColor(context, android.R.color.white))
            progressbar.indeterminateDrawable = DrawableCompat.unwrap(wrap)
        }
        else {
            progressbar.indeterminateDrawable.setColorFilter(ContextCompat.getColor(context, android.R.color.white), PorterDuff.Mode.SRC_IN)
            //TODO refazer para kotlin
        }
    }

    override fun setOnClickListener(l: OnClickListener?) {
        button.setOnClickListener(l)
    }

    override fun setEnabled(enabled: Boolean) {
        button.isEnabled = enabled
    }

    fun showProgress(enabled: Boolean) {
        progressbar.isVisible = enabled
        button.text = if (enabled) "" else text
    }
}