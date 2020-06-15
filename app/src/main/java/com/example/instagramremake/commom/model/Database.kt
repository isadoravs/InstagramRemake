package com.example.instagramremake.commom.model

import android.net.Uri
import android.os.Handler
import android.util.Log
import java.lang.Exception

class Database {
    companion object {
        var userAuth: UserAuth? = UserAuth("10", "user@email.com", "12345")
        private var usersAuth: HashSet<UserAuth> = HashSet()
        private var users: HashSet<User> = HashSet()
        private var storage: HashSet<Uri> = HashSet()
        private var posts: HashMap<String, HashSet<Post>> = HashMap()
        private var feed: HashMap<String, HashSet<Feed>> = HashMap()
        private var followers: HashMap<String, HashSet<String>> = HashMap()

        init {
//        userAuth = UserAuth("2", "user@email.com", "12345")
//        userAuth?.uuid?.let {
//            users.add(User(it, "user1@email.com", "123456"))
//        }
            users.add(User("1", "user1@email.com", "isadora1"))
            users.add(User("2", "user2@email.com", "isadora2"))
            users.add(User("3", "user3@email.com", "isadora3"))
        }
    }

    var onSuccessListener: ((response: Any) -> Unit)? = null
    var onFailureListener: ((error: Exception) -> Unit)? = null
    var onCompleteListener: (() -> Unit)? = null


    fun addPhoto(uuid: String, uri: Uri): Database {
        Handler().postDelayed({
            val user = users.find { it.uuid == uuid }
            user?.uri = uri

            storage.add(uri)
            onSuccessListener?.invoke(true)
        }, 2000)
        return this
    }

    fun createUser(email: String, name: String, password: String) {
        Handler().postDelayed({
            val userAuth = UserAuth("1", email, password)
            usersAuth.add(userAuth)

            val user = User(userAuth.uuid, email, name, null)
            val added = users.add(user)

            if (added) {
                Database.userAuth = userAuth

                followers[userAuth.uuid] = HashSet<String>()
                feed[userAuth.uuid] = HashSet<Feed>()

                onSuccessListener?.invoke(userAuth)
            } else {
                Database.userAuth = null
                onFailureListener?.invoke(IllegalArgumentException("Usuário já existe"))
            }
            onCompleteListener?.invoke()

        }, 2000)
    }

    fun following(uuidMe: String, uuid: String) {
        Handler().postDelayed({
            val followersUser = followers[uuid] ?: HashSet()
            val following = followersUser.contains(uuidMe)
            println("FOLLOWING $following")

            onSuccessListener?.invoke(following)
            onCompleteListener?.invoke()

        }, 2000)
    }

    fun follow(uuidMe: String, uuid: String) {
        Handler().postDelayed({
            val followersUser = followers[uuid] ?: HashSet()

            followersUser.add(uuidMe)

            onSuccessListener?.invoke(true)
            //onCompleteListener?.invoke()

        }, 2000)
    }

    fun unfollow(uuidMe: String, uuid: String) {
        Handler().postDelayed({
            val followersUser = followers[uuid] ?: HashSet()

            followersUser.remove(uuidMe)

            onSuccessListener?.invoke(true)
            //onCompleteListener?.invoke()

        }, 2000)
    }

    fun login(email: String, password: String) {
        Handler().postDelayed({
            val userAuth = UserAuth("1", email, password)
            if (usersAuth.contains(userAuth)) {
                Database.userAuth = userAuth
                onSuccessListener?.invoke(userAuth)
            } else {
                onFailureListener?.invoke(IllegalArgumentException("Usuário não encontrado"))
            }
            onCompleteListener?.invoke()
        }, 2000)
    }

    fun findPosts(uuid: String) {
        Handler().postDelayed({
            val response = posts[uuid] ?: HashSet()

            Log.e("aqui", "invoke findpost")
            Log.e("aqui", response.javaClass.toString())
            onSuccessListener?.invoke(ArrayList(response))
            onCompleteListener?.invoke()
        }, 2000)
    }

    fun findUser(uuid: String) {
        Handler().postDelayed({
            val response = users.find {
                it.uuid == uuid
            }

            Log.e("aqui", "invoke findUser")
            Log.e("aqui", response?.javaClass.toString())

            if (response != null) onSuccessListener?.invoke(response)
            else onFailureListener?.invoke(java.lang.IllegalArgumentException("Usuário não encontrado"))
            onCompleteListener?.invoke()

        }, 2000)
    }

    fun findUsers(uuid: String, query: String) {
        Handler().postDelayed({
            val response = users.filter { it.uuid != uuid && it.name.contains(query) }
            onSuccessListener?.invoke(ArrayList(response))
            onCompleteListener?.invoke()
        }, 2000)
    }


    fun findFeed(uuid: String) {
        Handler().postDelayed({
            val response = feed[uuid] ?: HashSet()

            Log.e("aqui", "invoke findFeed")
            Log.e("aqui", response.javaClass.toString())

            onSuccessListener?.invoke(ArrayList(response))
            onCompleteListener?.invoke()
        }, 2000)
    }

    fun createPost(uuid: String, uri: Uri, caption: String) {
        Handler().postDelayed({
            var posts = Database.posts[uuid]
            if (posts == null) {
                posts = HashSet()
                Database.posts[uuid] = posts
            }
            val post = Post(hashCode().toString(), uri, caption, System.currentTimeMillis())
            posts.add(post)

            var followers = followers[uuid]
            if (followers == null) {
                followers = HashSet()
                Database.followers[uuid] = followers
            } else {
                followers.map { follower ->
                    val feeds = feed[follower]
                    if (feeds != null) {
                        val feed = Feed(
                            User(uuid, "email@gmail.com", "Isadora V"),
                            Post(post.uuid, post.uri, post.caption, post.timestamp)
                        )
                        feeds.add(feed)
                    }
                }
                val feedMe = feed[uuid]
                if (feedMe != null) {
                    val feed = Feed(
                        User(uuid, "dois@gmail.com", "Isadora Dois"),
                        Post(post.uuid, post.uri, post.caption, post.timestamp)
                    )
                    feedMe.add(feed)
                }
            }

            Log.e("aqui", "invoke createPost")
            Log.e("aqui", "true")

            onSuccessListener?.invoke(true)
            onCompleteListener?.invoke()

        }, 2000)
    }
}