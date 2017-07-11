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


class ApiManager(val context: Context, val sharedPrefs: SharedPrefs) {

    companion object {
        private val AUTH_TOKEN_REFRESH_TIMEOUT_IN_MS = (55 * 60 * 1000).toLong()
    }

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
        currentUser?.getToken(true)
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("ApiManager", "Firebase auth token updated")
                        val authToken = task.result.token
                        sharedPrefs.setAuthToken(authToken!!)
                        execute(baseBaseRequest)
                    } else {
                        Log.d("ApiManager", "Firebase update auth token failed")
                    }
                }
                ?.addOnFailureListener { e -> Log.e("ApiManager", "Firebase update auth failed", e) } ?: Log.d("ApiManager", "Firebase update auth token failed, user not available")
    }

    private fun <T> execute(baseBaseRequest: BaseRequest<T>) {
        val subscription = baseBaseRequest.getObservable()
                .doOnNext { item -> baseBaseRequest.runOnBackgroundThread(item) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ },
                        { throwable -> baseBaseRequest.onFail(throwable) },
                        { baseBaseRequest.onComplete() })
        compositeDisposable.add(subscription)
    }
}