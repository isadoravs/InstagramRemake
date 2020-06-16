package com.example.instagramremake.login.datasource

import com.example.instagramremake.commom.presenter.Presenter
import com.google.firebase.auth.FirebaseAuth

class LoginFireDataSource: LoginDataSource {
    override fun onLogin(email: String, password: String, presenter: Presenter) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult -> authResult.user?.let { presenter.onSuccess(it) } }
            .addOnFailureListener{ e -> e.message?.let { presenter.onError(it) } }
            .addOnCompleteListener{ presenter.onComplete() }
    }
}