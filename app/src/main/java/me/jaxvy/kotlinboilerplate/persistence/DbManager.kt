package me.jaxvy.kotlinboilerplate.persistence

import android.annotation.SuppressLint
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class DbManager {

    @SuppressLint("CheckResult")
    fun runOnBackgroundThread(action: (() -> Unit), onError: ((Throwable) -> Unit)? = null) {
        Completable.fromCallable { action() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({}, { throwable -> onError?.invoke(throwable) })
    }
}
