package com.example.instagramremake.main.camera.presentation

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.example.instagramremake.R
import com.example.instagramremake.main.camera.datasource.AddFireDataSource
import com.example.instagramremake.main.camera.datasource.AddLocalDataSource
import kotlinx.android.synthetic.main.activity_add_caption.*

class AddCaptionActivity : AppCompatActivity(), AddCaptionView {

    private lateinit var presenter: AddPresenter
    private var uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_caption)

        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            it.setDisplayHomeAsUpEnabled(true)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        uri = intent.extras?.getParcelable<Uri>("uri")
        main_add_caption_image_view.setImageURI(uri)

        val dataSource = AddFireDataSource()
        presenter = AddPresenter(this, dataSource)

    }

    override fun postSaved() {
        finish()
    }

    override fun showProgressBar() {
        add_progress.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        add_progress.visibility = View.GONE
    }

    override fun getContext(): Context? {
        return getContext()
    }

    override fun setStatusBarDark() {
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.action_share -> uri?.let {
                presenter.createPost(it, main_add_caption_edit_text.text.toString())
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_share, menu)
        return super.onCreateOptionsMenu(menu)
    }

    companion object {
        fun launch(context: Context, uri: Uri) {
            val intent = Intent(context, AddCaptionActivity::class.java)
            intent.putExtra("uri", uri)
            context.startActivity(intent)
        }
    }
}