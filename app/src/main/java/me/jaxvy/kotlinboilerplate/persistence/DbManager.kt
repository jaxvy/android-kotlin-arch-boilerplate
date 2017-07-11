package me.jaxvy.kotlinboilerplate.persistence

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class DbManager {

    fun runOnBackgroundThread(action: (() -> Unit), onError: ((Throwable) -> Unit)? = null) {
        Observable.empty<Unit>()
                .doOnComplete { action() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({}, { throwable -> onError?.invoke(throwable) })
    }
}