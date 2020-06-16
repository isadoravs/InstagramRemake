package com.example.instagramremake.main.camera.datasource

import android.net.Uri
import com.example.instagramremake.commom.model.Feed
import com.example.instagramremake.commom.model.Post
import com.example.instagramremake.commom.model.User
import com.example.instagramremake.commom.presenter.Presenter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class AddFireDataSource : AddDataSource {
    override fun savePost(uri: Uri, caption: String, presenter: Presenter) {
        val host = uri.host
        var uriPost = uri
        if (host == null) { //from gallery
            uriPost = Uri.fromFile(File(uri.toString()))
        }
        val uid = FirebaseAuth.getInstance().uid
        val storageRef = FirebaseStorage.getInstance().reference
        if (uid != null && uriPost.lastPathSegment != null) {
            val imgRef = storageRef.child("/images")
                .child(uid)
                .child(uriPost.lastPathSegment!!)
            imgRef.putFile(uriPost)
                .addOnSuccessListener { taskSnapshot ->
                    imgRef.downloadUrl.addOnSuccessListener { uriResponse ->
                        val postRef = FirebaseFirestore.getInstance()
                            .collection("posts")
                            .document(uid)
                            .collection("posts")
                            .document()
                        val post = Post(
                            postRef.id,
                            null,
                            caption,
                            System.currentTimeMillis(),
                            uriResponse.toString()
                        )
                        postRef.set(post)
                            .addOnSuccessListener {
                                val me = FirebaseFirestore.getInstance()
                                    .collection("user")
                                    .document(uid)
                                FirebaseFirestore.getInstance().runTransaction { transaction ->
                                    val snapshot = transaction.get(me)
                                    val user = snapshot.toObject(User::class.java)
                                    val posts = user?.posts?.plus(1)
                                    transaction.update(me, "posts", posts)
                                    return@runTransaction
                                }
                                me.get().addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        val meUser = task.result?.toObject(User::class.java)

                                        FirebaseFirestore.getInstance()
                                            .collection("followers")
                                            .document(uid)
                                            .collection("followers")
                                            .get()
                                            .addOnCompleteListener { result ->
                                                if (result.isSuccessful) {
                                                    result.result?.documents?.forEach { doc ->
                                                        val user = doc.toObject(User::class.java)
                                                        val feed = meUser?.let { it ->
                                                            Feed(it, post)
                                                        }
                                                        if (feed != null && user != null) {
                                                            FirebaseFirestore.getInstance()
                                                                .collection("feed")
                                                                .document(user.uuid)
                                                                .collection("posts")
                                                                .document(postRef.id)
                                                                .set(feed)
                                                        }
                                                    }
                                                }
                                            }


                                    }
                                }


                                val id = postRef.id
                                FirebaseFirestore.getInstance()
                                    .collection("user")
                                    .document(uid)
                                    .get()
                                    .addOnCompleteListener { task ->
                                        val user = task.result?.toObject(User::class.java)
                                        val feed = user?.let { it1 -> Feed(it1, post) }
                                        if (feed != null) {
                                            FirebaseFirestore.getInstance()
                                                .collection("feed")
                                                .document(uid)
                                                .collection("posts")
                                                .document(id)
                                                .set(feed)
                                        }
                                        presenter.onSuccess(true)
                                        presenter.onComplete()
                                    }
                                    .addOnFailureListener { e ->
                                        e.message?.let {
                                            presenter.onError(
                                                it
                                            )
                                        }
                                    }
                            }
                            .addOnFailureListener { e -> e.message?.let { presenter.onError(it) } }
                    }
                }
                .addOnFailureListener { e -> e.message?.let { presenter.onError(it) } }
        }


    }
}