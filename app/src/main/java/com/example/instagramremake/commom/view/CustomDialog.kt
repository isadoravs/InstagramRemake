package com.example.instagramremake.commom.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.StringRes
import com.example.instagramremake.R
import com.example.instagramremake.register.presentation.RegisterPhotoFragment
import kotlinx.android.synthetic.main.dialog_custom.*

class CustomDialog(context: Context) : Dialog(context) {

    private lateinit var textViews: Array<TextView?>
    private lateinit var layoutParams: LinearLayout.LayoutParams
    private var titleId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_custom)

        layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(30, 50, 30, 50)
    }

    override fun setTitle(titleId: Int) {
        this.titleId = titleId
    }

    override fun show() {
        super.show()
        dialog_title.setText(titleId)
        textViews.forEach {
            dialog_custom.addView(it, layoutParams)
        }
    }

    private fun addButton(listener: View.OnClickListener, @StringRes vararg texts: Int): Unit{
        textViews = arrayOfNulls<TextView>(texts.size)
        texts.forEachIndexed { index, id ->
            val textView = TextView(ContextThemeWrapper(context, R.style.TextViewBaseDialog), null, 0)
            textView.id = id
            textView.setText(id)
            textView.setOnClickListener {
                listener.onClick(it)
                dismiss()
            }
            textViews[index] = textView
        }
    }

    class Builder(private val context: Context) {
        @StringRes
        private var titleId: Int = 0
        private lateinit var listener: View.OnClickListener
        private lateinit var texts: IntArray

        fun setTitle(@StringRes id: Int): Builder{
            this.titleId = id
            return this
        }

        fun addButton(listener: View.OnClickListener, @StringRes vararg texts: Int): Builder{
            this.listener = listener
            this.texts = texts
            return this
        }

        fun build(): CustomDialog {
            val customDialog = CustomDialog(context)
            customDialog.setTitle(titleId)
            customDialog.addButton(listener, *texts)
            return customDialog
        }

    }
}