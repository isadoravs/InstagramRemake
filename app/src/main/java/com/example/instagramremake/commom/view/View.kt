package com.example.instagramremake.commom.view

import android.content.Context

interface View {
    fun showProgressBar()
    fun hideProgressBar()
    fun getContext(): Context?
    fun setStatusBarDark()
    }