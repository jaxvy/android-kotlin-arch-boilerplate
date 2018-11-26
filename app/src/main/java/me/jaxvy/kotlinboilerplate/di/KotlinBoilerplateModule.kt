package me.jaxvy.kotlinboilerplate.di

import androidx.room.Room
import android.content.Context
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import me.jaxvy.kotlinboilerplate.api.Api
import me.jaxvy.kotlinboilerplate.api.ApiManager
import me.jaxvy.kotlinboilerplate.api.NetworkSettings
import me.jaxvy.kotlinboilerplate.persistence.Db
import me.jaxvy.kotlinboilerplate.persistence.DbManager
import me.jaxvy.kotlinboilerplate.persistence.SharedPrefs
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class KotlinBoilerplateModule(val applicationContext: Context) {

    @Provides
    fun provideContext(): Context = applicationContext;

    @Provides
    @Singleton
    fun provideSharedPrefs(context: Context) = SharedPrefs(context)

    @Provides
    @Singleton
    fun provideNetworkSettings(sharedPrefs: SharedPrefs): NetworkSettings {
        return NetworkSettings(sharedPrefs)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(networkSettings: NetworkSettings): OkHttpClient {
        val builder = OkHttpClient.Builder()
        val interceptors = networkSettings.getInterceptors()
        builder.interceptors().addAll(interceptors)
        return builder.build()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient, networkSettings: NetworkSettings): Retrofit {
        return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(networkSettings.getBaseUrl())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
    }

    @Provides
    @Singleton
    fun provideApiRetrofitService(retrofit: Retrofit): Api {
        return retrofit.create<Api>(Api::class.java)
    }

    @Provides
    @Singleton
    fun provideManager(context: Context, sharedPrefs: SharedPrefs): ApiManager {
        return ApiManager(context, sharedPrefs)
    }

    @Provides
    @Singleton
    fun provideDb(context: Context): Db {
        return Room.databaseBuilder(context, Db::class.java, "kotlinboilerplate-db").build()
    }

    @Provides
    @Singleton
    fun provideDbManager() = DbManager()
}