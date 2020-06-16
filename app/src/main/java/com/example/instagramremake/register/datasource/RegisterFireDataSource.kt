package com.example.instagramremake.register.datasource

import android.net.Uri
import android.util.Log
import com.example.instagramremake.commom.model.User
import com.example.instagramremake.commom.presenter.Presenter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class RegisterFireDataSource : RegisterDataSource {
    override fun createUser(name: String, email: String, password: String, presenter: Presenter) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                val user = authResult.user?.let {
                    User(it.uid, email, name)
                }
                user?.let {
                    FirebaseFirestore.getInstance().collection("user")
                        .document(it.uuid).set(it)
                        .addOnSuccessListener { _ -> presenter.onSuccess(it) }
                        .addOnCompleteListener { presenter.onComplete() }
                }
            }
            .addOnFailureListener { e ->
                e.message?.let { presenter.onError(it) }
                presenter.onComplete()
            }


    }

    override fun addPhoto(uri: Uri, presenter: Presenter) {
        val uid = FirebaseAuth.getInstance().uid
        if (uid == null || uri.lastPathSegment == null) {
            return
        }
        val storageRef = FirebaseStorage.getInstance().reference
        val imgRef = storageRef.child("images/")
            .child(uid)
            .child(uri.lastPathSegment!!)

        imgRef.putFile(uri).addOnSuccessListener {
            val totalByteCount = it.totalByteCount
            Log.i("teste", "file upload size $totalByteCount")

            imgRef.downloadUrl.addOnSuccessListener { uriResponse ->
                val docUser = FirebaseFirestore.getInstance().collection("user").document(uid)

                docUser.get().addOnSuccessListener { documentSnapshot ->
                    val user = documentSnapshot.toObject(User::class.java)
                    user?.photoUrl = uriResponse.toString()
                    if (user != null) {
                        docUser.set(user).addOnSuccessListener {
                            presenter.onSuccess(true)
                            presenter.onComplete()
                        }
                    }
                }
            }
        }
    }
}