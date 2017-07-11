package me.jaxvy.kotlinboilerplate.ui.common

import android.arch.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import me.jaxvy.kotlinboilerplate.KotlinBoilerplateApplication
import me.jaxvy.kotlinboilerplate.api.Api
import me.jaxvy.kotlinboilerplate.api.ApiManager
import me.jaxvy.kotlinboilerplate.persistence.Db
import me.jaxvy.kotlinboilerplate.persistence.DbManager
import me.jaxvy.kotlinboilerplate.persistence.SharedPrefs
import javax.inject.Inject

abstract class BaseViewModel : ViewModel {

    @Inject
    protected lateinit var sharedPrefs: SharedPrefs

    @Inject
    protected lateinit var api: Api

    @Inject
    protected lateinit var db: Db

    @Inject
    protected lateinit var dbManager : DbManager

    @Inject
    protected lateinit var apiManager: ApiManager

    protected val firebaseAuth: FirebaseAuth

    constructor() : super() {
        KotlinBoilerplateApplication.kotlinBoilerplateComponent.inject(this)
        firebaseAuth = FirebaseAuth.getInstance()
    }
}