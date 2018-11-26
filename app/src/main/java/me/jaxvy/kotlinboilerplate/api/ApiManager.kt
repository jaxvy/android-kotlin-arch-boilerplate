package me.jaxvy.kotlinboilerplate.api

import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import me.jaxvy.kotlinboilerplate.KotlinBoilerplateApplication
import me.jaxvy.kotlinboilerplate.api.request.BaseRequest
import me.jaxvy.kotlinboilerplate.persistence.SharedPrefs
import java.util.*

private const val AUTH_TOKEN_REFRESH_TIMEOUT_IN_MS = (55 * 60 * 1000).toLong()

class ApiManager(val context: Context, val sharedPrefs: SharedPrefs) {

    private val compositeDisposable = CompositeDisposable()

    fun <T> call(baseBaseRequest: BaseRequest<T>) {
        KotlinBoilerplateApplication.kotlinBoilerplateComponent.inject(baseBaseRequest)
        if (refreshAuthToken()) {
            updateTokenAndExecute(baseBaseRequest)
        } else {
            execute(baseBaseRequest)
        }
    }

    private fun refreshAuthToken(): Boolean {
        val authTokenTimeCreate = sharedPrefs.getAuthTokenTimeCreate()
        val now = Date().time
        return now - authTokenTimeCreate > AUTH_TOKEN_REFRESH_TIMEOUT_IN_MS
    }

    private fun <T> updateTokenAndExecute(baseBaseRequest: BaseRequest<T>) {
        Log.d("ApiManager", "Firebase auth token expired, updating...")
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.getIdToken(true)
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("ApiManager", "Firebase auth token updated")
                        task.result?.token?.run { sharedPrefs.setAuthToken(this) }
                        execute(baseBaseRequest)
                    } else {
                        Log.d("ApiManager", "Firebase update auth token failed")
                    }
                }
                ?.addOnFailureListener { e ->
                    Log.e("ApiManager", "Firebase update auth failed", e)
                }
                ?: Log.d("ApiManager", "Firebase update auth token failed, user not available")
    }

    private fun <T> execute(request: BaseRequest<T>) {
        val subscription = request.getSingle()
                .doOnSuccess { item -> request.databaseOperationCallback(item) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { request.onComplete() },
                        { throwable -> request.onFail(throwable) }
                )
        compositeDisposable.add(subscription)
    }
}