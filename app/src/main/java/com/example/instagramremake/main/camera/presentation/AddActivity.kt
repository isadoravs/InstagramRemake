package com.example.instagramremake.main.camera.presentation

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.instagramremake.R
import com.example.instagramremake.main.camera.datasource.GalleryLocalDataSource
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_add.*

class AddActivity : AppCompatActivity(), AddView {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setHomeAsUpIndicator(R.drawable.ic_close)
            it.setDisplayHomeAsUpEnabled(true)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        onInject()
        add_tab_layout.addOnTabSelectedListener(object :
            TabLayout.ViewPagerOnTabSelectedListener(add_viewpager) {
            override fun onTabSelected(tab: TabLayout.Tab) {
                add_viewpager.currentItem = tab.position
            }
        })
    }

    private fun onInject() {
        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        add_viewpager.adapter = viewPagerAdapter

        val galleryDataSource = GalleryLocalDataSource()
        val galleryPresenter = GalleryPresenter(galleryDataSource)

        val galleryFragment = GalleryFragment.newInstance(this, galleryPresenter)
        viewPagerAdapter.add(galleryFragment)

        val cameraFragment = CameraFragment.newInstance(this)
        viewPagerAdapter.add(cameraFragment)

        viewPagerAdapter.notifyDataSetChanged()
        add_tab_layout.setupWithViewPager(add_viewpager)

        val tabLeft = add_tab_layout.getTabAt(0)
        tabLeft?.text = getString(R.string.gallery)

        val tabRight = add_tab_layout.getTabAt(1)
        tabRight?.text = getString(R.string.photo)

        add_viewpager.currentItem = viewPagerAdapter.count - 1
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onImageLoaded(uri: Uri) {
        AddCaptionActivity.launch(this, uri)
        finish()
    }

    override fun dispose() {
        finish()
    }

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, AddActivity::class.java)
            context.startActivity(intent)
        }
    }

}