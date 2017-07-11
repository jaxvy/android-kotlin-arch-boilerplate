package me.jaxvy.kotlinboilerplate.api.request

import io.reactivex.Observable

abstract class BaseRequest<T> : InjectionBase {

    var onSuccess: (() -> Unit)? = null
    var onError: ((Throwable) -> Unit)? = null
    var tag: String? = null

    constructor(onSuccess: (() -> Unit)?,
                onError: ((Throwable) -> Unit)?) : super() {
        this.onSuccess = onSuccess
        this.onError = onError
    }

    constructor(onSuccess: (() -> Unit)?,
                onError: ((Throwable) -> Unit)?,
                tag: String?) : super() {
        this.onSuccess = onSuccess
        this.onError = onError
        this.tag = tag
    }

    constructor(onSuccess: (() -> Unit)?) : super() {
        this.onSuccess = onSuccess
    }

    constructor(onError: ((Throwable) -> Unit)?) : super() {
        this.onError = onError
    }

    constructor() : super()

    abstract fun getObservable(): Observable<T>

    fun onComplete() {
        onSuccess?.invoke()
    }

    fun onFail(throwable: Throwable) {
        onError?.invoke(throwable)
    }

    /**
     * Database operations (for caching incoming response data) is performed on the IO thread. This
     * method is the callback to implement db operation on Room
     */
    abstract fun runOnBackgroundThread(t: T)
}