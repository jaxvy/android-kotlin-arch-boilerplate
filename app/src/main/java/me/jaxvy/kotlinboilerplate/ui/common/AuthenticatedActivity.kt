package me.jaxvy.kotlinboilerplate.ui.common

import android.content.Intent
import android.os.Bundle
import me.jaxvy.kotlinboilerplate.ui.login.LoginActivity
import me.jaxvy.kotlinboilerplate.utils.isUserLoggedIn

abstract class AuthenticatedActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!firebaseAuth.isUserLoggedIn()) {
            var intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}