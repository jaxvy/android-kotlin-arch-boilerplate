package me.jaxvy.kotlinboilerplate.api.request

import com.google.firebase.auth.FirebaseAuth
import me.jaxvy.kotlinboilerplate.api.Api
import me.jaxvy.kotlinboilerplate.persistence.Db
import me.jaxvy.kotlinboilerplate.persistence.SharedPrefs
import javax.inject.Inject

/**
 * Need this class because last time I checked dagger2 still couldn't support injecting into
 * classes with generic parameters. Used by BaseRequest class
 */
abstract class InjectionBase {

    @Inject
    protected lateinit var sharedPrefs: SharedPrefs

    @Inject
    protected lateinit var api: Api

    @Inject
    protected lateinit var db: Db

    protected val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
}