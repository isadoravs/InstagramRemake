package com.example.instagramremake.commom.presenter

interface Presenter {
    fun onSuccess(response: Any)
    fun onError(message: String)
    fun onComplete()
}