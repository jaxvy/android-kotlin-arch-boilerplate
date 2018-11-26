package me.jaxvy.kotlinboilerplate.ui.common

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth

abstract class BaseActivity : AppCompatActivity() {

    protected lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
    }

    protected fun setupToolbar(toolbar: Toolbar, @StringRes title: Int) {
        setSupportActionBar(toolbar)
        actionBar?.setTitle(title)
    }
}