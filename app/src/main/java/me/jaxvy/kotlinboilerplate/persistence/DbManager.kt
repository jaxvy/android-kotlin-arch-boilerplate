package me.jaxvy.kotlinboilerplate.persistence

import android.annotation.SuppressLint
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class DbManager {

    @SuppressLint("CheckResult")
    fun runOnBackgroundThread(action: (() -> Unit), onError: ((Throwable) -> Unit)? = null) {
        Observable.fromCallable { action() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({}, { throwable -> onError?.invoke(throwable) })
    }
}