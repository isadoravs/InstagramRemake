package com.example.instagramremake.main.search.datasource

import com.example.instagramremake.commom.model.User
import com.example.instagramremake.commom.presenter.Presenter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SearchFireDataSource : SearchDataSource {
    override fun findUsers(query: String, presenter: Presenter) {
        FirebaseFirestore.getInstance()
            .collection("user")
            .whereEqualTo("name", query)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val users = querySnapshot.documents.map { doc ->
                    doc.toObject(User::class.java)
                }.filter {
                    it?.uuid != FirebaseAuth.getInstance().uid
                }
                presenter.onSuccess(users)
            }
            .addOnFailureListener { e -> e.message?.let { presenter.onError(it) } }
            .addOnCompleteListener { presenter.onComplete() }
    }
}