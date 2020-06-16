package com.example.instagramremake.main.home.datasource

import com.example.instagramremake.commom.model.Feed
import com.example.instagramremake.commom.presenter.Presenter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage

class HomeFireDataSource: HomeDataSource {
    override fun findFeed(presenter: Presenter) {
        val uid = FirebaseAuth.getInstance().uid
        println("AKI UID $uid")
        if (uid != null) {
            FirebaseFirestore.getInstance()
                .collection("feed")
                .document(uid)
                .collection("posts")
                .orderBy("post.timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    val feed = querySnapshot.documents.map {
                        it.toObject(Feed::class.java)
                    }
                    presenter.onSuccess(feed)
                }
                .addOnFailureListener { e -> e.message?.let { presenter.onError(it) } }
                .addOnCompleteListener{ presenter.onComplete() }
        }
    }

}