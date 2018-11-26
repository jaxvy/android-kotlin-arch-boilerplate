package me.jaxvy.kotlinboilerplate.api.request

import io.reactivex.Single

abstract class BaseRequest<T>(
        private var onSuccess: (() -> Unit)? = null,
        private var onError: ((Throwable) -> Unit)? = null
) : InjectionBase() {

    abstract fun getSingle(): Single<T>

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
    open fun databaseOperationCallback(t: T) {}
}