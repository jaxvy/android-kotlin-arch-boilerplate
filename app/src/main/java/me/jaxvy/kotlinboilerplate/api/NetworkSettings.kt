package me.jaxvy.kotlinboilerplate.api

import me.jaxvy.kotlinboilerplate.BuildConfig
import me.jaxvy.kotlinboilerplate.persistence.SharedPrefs
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor

class NetworkSettings(private val sharedPrefs: SharedPrefs) {

    fun getInterceptors(): ArrayList<Interceptor> {
        val interceptors = ArrayList<Interceptor>()
        if (BuildConfig.DEBUG) {
            interceptors.add(getLoggingInterceptor())
        }
        interceptors.add(Interceptor { chain ->
            var request = chain.request()
            sharedPrefs.getAuthToken()?.run {
                val url = request.url()
                        .newBuilder()
                        .addQueryParameter("auth", this)
                        .build()
                request = request.newBuilder().url(url).build()
            }
            chain.proceed(request)
        })

        return interceptors
    }

    private fun getLoggingInterceptor(): Interceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }

    fun getBaseUrl(): String {
        return BuildConfig.BASE_SERVICE_URL
    }
}