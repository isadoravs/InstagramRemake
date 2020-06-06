package com.example.instagramremake.commom.model

import android.net.Uri
import android.os.Handler
import java.lang.Exception

object Database {

    var userAuth: UserAuth? = null
    private var usersAuth: MutableSet<UserAuth> = HashSet()
    private var users: MutableSet<User> = HashSet()
    private var storage: HashSet<Uri> = HashSet()

    var onSuccessListener: ((response: Any) -> Unit)? = null
    var onFailureListener: ((error: Exception) -> Unit)? = null
    var onCompleteListener: (() -> Unit)? = null

    init {
        userAuth = UserAuth("1", "user@email.com", "12345")
        userAuth?.uuid?.let {
            users.add(User(it, "user1@email.com", "123456", null))
        }
//        usersAuth.add(UserAuth("3", "user2@email.com", "123457"))
//        usersAuth.add(UserAuth("4", "user3@email.com", "123458"))
//        usersAuth.add(UserAuth("5", "user4@email.com", "123459"))
    }

    fun addPhoto(uuid: String, uri: Uri): Database {
        Handler().postDelayed({
            val users = Database.users
            for (user in users) {
                if (user.uuid == uuid) {
                    user.uri = uri
                }
            }
            storage.add(uri)
            onSuccessListener?.invoke(true)
        }, 2000)
        return this
    }

    fun createUser(email: String, name: String, password: String): Database {
        Handler().postDelayed({
            val userAuth = UserAuth("1", email, password)
            usersAuth.add(userAuth)

            val user = User(userAuth.uuid, email, name, null)
            val added = users.add(user)

            if (added) {
                this.userAuth = userAuth
                onSuccessListener?.invoke(userAuth)
            } else {
                this.userAuth = null
                onFailureListener?.invoke(IllegalArgumentException("Usuário já existe"))
            }
            onCompleteListener?.invoke()

        }, 2000)

        return this
    }

    fun login(email: String, password: String): Database {
        Handler().postDelayed({
            val userAuth = UserAuth("1", email, password)
            if (usersAuth.contains(userAuth)) {
                this.userAuth = userAuth
                onSuccessListener?.invoke(userAuth)
            } else {
                onFailureListener?.invoke(IllegalArgumentException("Usuário não encontrado"))
            }
            onCompleteListener?.invoke()
        }, 2000)
        return this
    }
}