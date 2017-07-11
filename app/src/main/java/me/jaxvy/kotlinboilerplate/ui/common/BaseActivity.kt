package me.jaxvy.kotlinboilerplate.ui.common

import android.arch.lifecycle.LifecycleActivity
import android.os.Bundle
import android.support.annotation.StringRes
import android.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth

abstract class BaseActivity : LifecycleActivity() {

    protected lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
    }

    protected fun setupToolbar(toolbar: Toolbar, @StringRes title: Int) {
        setActionBar(toolbar)
        actionBar.setTitle(title)
    }
}