package me.jaxvy.kotlinboilerplate.ui.login

import me.jaxvy.kotlinboilerplate.ui.common.BaseViewModel


class LoginViewModel : BaseViewModel() {

    var isLoggingIn = false

    private var onLoginError: ((Exception?) -> (Unit))? = null

    fun registerLoginErrorHandler(onError: ((Exception?) -> (Unit))?) {
        onLoginError = onError
    }

    fun login(email: String, password: String, onSuccess: () -> Unit) {
        isLoggingIn = true
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        fetchAuthToken(onSuccess)
                    } else {
                        isLoggingIn = false
                        onLoginError?.invoke(task.exception)
                    }
                }
    }

    private fun fetchAuthToken(onSuccess: () -> Unit) {
        firebaseAuth.currentUser?.getIdToken(true)
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val authToken = task.result?.token
                        sharedPrefs.setAuthToken(authToken!!)
                        isLoggingIn = false
                        onSuccess.invoke()
                    } else {
                        isLoggingIn = false
                        onLoginError?.invoke(task.exception)
                    }
                }
    }

    override fun onCleared() {
        onLoginError = null
    }
}