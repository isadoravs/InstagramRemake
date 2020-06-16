package com.example.instagramremake.main.profile.datasource

import com.example.instagramremake.commom.model.Feed
import com.example.instagramremake.commom.model.Post
import com.example.instagramremake.commom.model.User
import com.example.instagramremake.commom.model.UserProfile
import com.example.instagramremake.commom.presenter.Presenter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ProfileFireDataSource : ProfileDataSource {
    override fun findUser(uuid: String, presenter: Presenter) {
        FirebaseFirestore.getInstance()
            .collection("user")
            .document(uuid)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val user = documentSnapshot.toObject(User::class.java)
                FirebaseFirestore.getInstance()
                    .collection("posts")
                    .document(uuid)
                    .collection("posts")
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        val posts = querySnapshot.documents.map {
                            it.toObject(Post::class.java)
                        }
                        FirebaseAuth.getInstance().uid?.let {
                            FirebaseFirestore.getInstance()
                                .collection("followers")
                                .document(uuid)
                                .collection("followers")
                                .document(it)
                                .get()
                                .addOnSuccessListener { documentSnapshotFollowers ->
                                    val following = documentSnapshotFollowers.exists()
                                    if (user != null) {
                                        presenter.onSuccess(
                                            UserProfile(
                                                user,
                                                posts as List<Post>, following
                                            )
                                        )
                                        presenter.onComplete()
                                    }
                                }
                                .addOnFailureListener { e ->
                                    e.message?.let { it1 ->
                                        presenter.onError(
                                            it1
                                        )
                                    }
                                }
                        }

                    }
                    .addOnFailureListener { e -> e.message?.let { it1 -> presenter.onError(it1) } }
            }
            .addOnFailureListener { e -> e.message?.let { it1 -> presenter.onError(it1) } }
    }

    override fun follow(uuid: String) {
        val toFollow = FirebaseFirestore.getInstance()
            .collection("user")
            .document(uuid)

        toFollow.get()
            .addOnCompleteListener { task ->
                val user = task.result?.toObject(User::class.java)

                FirebaseFirestore.getInstance().runTransaction { transaction ->
                    val followers = user?.followers?.plus(1)
                    transaction.update(toFollow, "followers", followers)
                    return@runTransaction
                }

                FirebaseAuth.getInstance().uid?.let { it1 ->
                    val fromFollow = FirebaseFirestore.getInstance()
                        .collection("user")
                        .document(it1)

                    fromFollow.get()
                        .addOnCompleteListener { task1 ->
                            val me = task1.result?.toObject(User::class.java)

                            FirebaseFirestore.getInstance().runTransaction { transaction ->
                                val following = me?.following?.plus(1)
                                transaction.update(fromFollow, "following", following)
                                return@runTransaction
                            }

                            FirebaseAuth.getInstance().uid?.let {
                                if (me != null) {
                                    FirebaseFirestore.getInstance()
                                        .collection("followers")
                                        .document(uuid)
                                        .collection("followers")
                                        .document(it)
                                        .set(me)
                                }
                            }
                        }
                }

                FirebaseFirestore.getInstance()
                    .collection("posts")
                    .document(uuid)
                    .collection("posts")
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .limit(10)
                    .get()
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            it.result?.documents?.forEach { doc ->
                                val post = doc.toObject(Post::class.java)
                                if (post != null && user != null) {
                                    val feed = Feed(user, post)

                                    FirebaseAuth.getInstance().uid?.let { it1 ->
                                        FirebaseFirestore.getInstance()
                                            .collection("feed")
                                            .document(it1)
                                            .collection("posts")
                                            .document(doc.id)
                                            .set(feed)
                                    }
                                }
                            }
                        }
                    }
            }
    }

    override fun unfollow(uuid: String) {
        val toUnfollow = FirebaseFirestore.getInstance()
            .collection("user")
            .document(uuid)

        toUnfollow.get()
            .addOnCompleteListener { task ->
                val user = task.result?.toObject(User::class.java)

                FirebaseFirestore.getInstance().runTransaction { transaction ->
                    val followers = user?.followers?.plus(1)
                    transaction.update(toUnfollow, "followers", followers)
                    return@runTransaction
                }

                val fromFollower =
                    FirebaseAuth.getInstance().uid?.let {
                        FirebaseFirestore.getInstance().collection("user")
                            .document(it)
                    }
                FirebaseFirestore.getInstance().runTransaction{transaction ->
                    fromFollower?.let {
                        val follower = transaction.get(it).toObject(User::class.java)
                        val following = follower?.following?.minus(1)
                        transaction.update(it, "following", following)
                    }
                    return@runTransaction

                }

                FirebaseAuth.getInstance().uid?.let {

                    FirebaseFirestore.getInstance()
                        .collection("followers")
                        .document(uuid)
                        .collection("followers")
                        .document(it)
                        .delete()

                    FirebaseFirestore.getInstance()
                        .collection("feed")
                        .document(it)
                        .collection("posts")
                        .whereEqualTo("publisher.uuid", uuid)
                        .get()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                task.result?.documents?.forEach { doc ->
                                    doc.reference.delete()
                                }
                            }
                        }
                }
            }
    }

}