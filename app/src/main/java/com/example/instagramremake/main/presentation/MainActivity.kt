package com.example.instagramremake.main.presentation

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import com.example.instagramremake.R
import com.example.instagramremake.login.presentation.LoginActivity
import com.example.instagramremake.main.camera.presentation.CameraFragment
import com.example.instagramremake.main.home.presentation.HomeFragment
import com.example.instagramremake.main.profile.presentation.ProfileFragment
import com.example.instagramremake.main.search.presentation.SearchFragment
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainView, BottomNavigationView.OnNavigationItemSelectedListener {
    private lateinit var active: Fragment

    private lateinit var homeFragment: HomeFragment
    private lateinit var searchFragment: SearchFragment
    private lateinit var cameraFragment: CameraFragment
    private lateinit var profileFragment: ProfileFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        setSupportActionBar(main_toolbar)
        supportActionBar?.let {
            it.setHomeAsUpIndicator(R.drawable.ic_insta_camera)
            it.setDisplayHomeAsUpEnabled(true)
        }

        navigationFragments()
    }

    companion object {
        const val LOGIN_ACTIVITY = 0
        const val REGISTER_ACTIVITY = 1
        const val ACT_SOURCE = "act_source"
        fun launch(context: Context, source: Int) {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra(ACT_SOURCE, source)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    private fun navigationFragments() {
        homeFragment = HomeFragment()
        profileFragment = ProfileFragment()
        cameraFragment = CameraFragment()
        searchFragment = SearchFragment()

        active = homeFragment

        supportFragmentManager.beginTransaction().add(R.id.main_fragment, profileFragment)
            .hide(profileFragment).commit()
        supportFragmentManager.beginTransaction().add(R.id.main_fragment, cameraFragment)
            .hide(cameraFragment).commit()
        supportFragmentManager.beginTransaction().add(R.id.main_fragment, searchFragment)
            .hide(searchFragment).commit()
        supportFragmentManager.beginTransaction().add(R.id.main_fragment, homeFragment)
            .hide(homeFragment).commit()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        main_bottom_nav.setOnNavigationItemSelectedListener(this)
        intent.extras?.let {
            when (it.getInt(ACT_SOURCE)) {
                REGISTER_ACTIVITY -> {
                    supportFragmentManager.beginTransaction().hide(active).show(profileFragment)
                        .commit()
                    active = profileFragment
                    scrollToolbarEnabled(true)
                }
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_bottom_home -> {
                supportFragmentManager.beginTransaction().hide(active).show(homeFragment).commit()
                active = homeFragment
                scrollToolbarEnabled(false)
                return true
            }
            R.id.menu_bottom_search -> {
                supportFragmentManager.beginTransaction().hide(active).show(searchFragment).commit()
                active = searchFragment
                return true
            }
            R.id.menu_bottom_add -> {
                supportFragmentManager.beginTransaction().hide(active).show(cameraFragment).commit()
                active = cameraFragment
                return true
            }
            R.id.menu_bottom_profile -> {
                supportFragmentManager.beginTransaction().hide(active).show(profileFragment).commit()
                active = profileFragment
                scrollToolbarEnabled(true)
                return true
            }
        }
        return false
    }

    override fun scrollToolbarEnabled(enabled: Boolean) {
        val toolbarParams = main_toolbar.layoutParams as AppBarLayout.LayoutParams
        val appBarParams = main_appbar.layoutParams as CoordinatorLayout.LayoutParams

        if(enabled){
            toolbarParams.scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
            appBarParams.behavior = AppBarLayout.Behavior()
            main_appbar.layoutParams = appBarParams
        } else {
            toolbarParams.scrollFlags = 0
            appBarParams.behavior = null
            main_appbar.layoutParams = appBarParams
        }

    }
}