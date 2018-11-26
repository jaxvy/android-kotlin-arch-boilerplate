package me.jaxvy.kotlinboilerplate.ui.common

import android.os.Bundle
import me.jaxvy.kotlinboilerplate.ui.login.LoginActivity
import me.jaxvy.kotlinboilerplate.utils.isUserLoggedIn
import me.jaxvy.kotlinboilerplate.utils.startActivity

abstract class AuthenticatedActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!firebaseAuth.isUserLoggedIn()) {
            startActivity<LoginActivity>()
            finish()
        }
    }
}