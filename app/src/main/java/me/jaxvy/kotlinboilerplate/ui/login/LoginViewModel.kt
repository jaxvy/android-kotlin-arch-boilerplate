package me.jaxvy.kotlinboilerplate.ui.login

import me.jaxvy.kotlinboilerplate.ui.common.BaseViewModel


class LoginViewModel : BaseViewModel() {

    internal var isLoggingIn = false

    private var onLoginSuccess: (() -> Unit)? = null
    private var onLoginError: ((Exception?) -> (Unit))? = null

    fun registerLoginHandlers(
            onLoginSuccess: (() -> Unit)?,
            onLoginError: ((Exception?) -> (Unit))?
    ) {
        this.onLoginSuccess = onLoginSuccess
        this.onLoginError = onLoginError
    }

    fun login(email: String, password: String) {
        isLoggingIn = true
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        fetchAuthToken()
                    } else {
                        isLoggingIn = false
                        onLoginError?.invoke(task.exception)
                    }
                }
    }

    private fun fetchAuthToken() {
        firebaseAuth.currentUser?.getIdToken(true)
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        task.result?.token?.run { sharedPrefs.setAuthToken(this) }
                        isLoggingIn = false
                        onLoginSuccess?.invoke()
                    } else {
                        isLoggingIn = false
                        onLoginError?.invoke(task.exception)
                    }
                }
    }

    override fun onCleared() {
        super.onCleared()
        onLoginSuccess = null
        onLoginError = null
    }
}
