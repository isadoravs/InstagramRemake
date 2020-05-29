package com.example.instagramremake.commom.model

import android.os.Handler
import java.lang.Exception

object Database {

    private lateinit var userAuth: UserAuth
    private var usersAuth: MutableSet<UserAuth> = HashSet()

    var onSuccessListener: ((response: Any) -> Unit)? = null
    var onFailureListener: ((error: Exception) -> Unit)? = null
    var onCompleteListener: (() -> Unit)? = null

    init {
        usersAuth.add(UserAuth("user@email.com", "12345"))
        usersAuth.add(UserAuth("user1@email.com", "123456"))
        usersAuth.add(UserAuth("user2@email.com", "123457"))
        usersAuth.add(UserAuth("user3@email.com", "123458"))
        usersAuth.add(UserAuth("user4@email.com", "123459"))
        usersAuth.add(UserAuth("user5@email.com", "123451"))
        usersAuth.add(UserAuth("user6@email.com", "123452"))
        usersAuth.add(UserAuth("user7@email.com", "123453"))
    }

    fun login(email: String, password: String): Database {
        Handler().postDelayed({
            val userAuth = UserAuth(email, password)
            if (usersAuth.contains(userAuth)) {
                this.userAuth = userAuth
                this.onSuccessListener?.invoke(userAuth)
            } else {
                this.onFailureListener?.invoke(IllegalArgumentException("Usuário não encontrado"))
            }
            this.onCompleteListener?.invoke()
        }, 2000)
        return this
    }
}