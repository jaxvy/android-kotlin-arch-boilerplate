package me.jaxvy.kotlinboilerplate.api

import me.jaxvy.kotlinboilerplate.BuildConfig
import me.jaxvy.kotlinboilerplate.persistence.SharedPrefs
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor

class NetworkSettings(val mSharedPrefs: SharedPrefs) {

    fun getInterceptors(): ArrayList<Interceptor> {
        val interceptors = ArrayList<Interceptor>()
        if (BuildConfig.DEBUG) {
            interceptors.add(getLoggingInterceptor())
        }
        interceptors.add(Interceptor {
            chain ->
            var request = chain.request()
            val authToken = mSharedPrefs.getAuthToken()
            if (authToken != null) {
                val url = request.url()
                        .newBuilder()
                        .addQueryParameter("auth", authToken)
                        .build()
                request = request.newBuilder().url(url).build()
            }
            chain.proceed(request)
        })

        return interceptors
    }

    fun getLoggingInterceptor(): Interceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }

    fun getBaseUrl(): String {
        return BuildConfig.BASE_SERVICE_URL
    }
}