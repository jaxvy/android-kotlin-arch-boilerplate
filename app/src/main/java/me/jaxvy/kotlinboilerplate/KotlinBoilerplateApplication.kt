package me.jaxvy.kotlinboilerplate

import android.app.Application
import me.jaxvy.kotlinboilerplate.di.DaggerKotlinBoilerplateComponent
import me.jaxvy.kotlinboilerplate.di.KotlinBoilerplateComponent
import me.jaxvy.kotlinboilerplate.di.KotlinBoilerplateModule

class KotlinBoilerplateApplication : Application() {

    companion object {
        @JvmStatic
        lateinit var kotlinBoilerplateComponent: KotlinBoilerplateComponent
    }

    override fun onCreate() {
        super.onCreate()

        kotlinBoilerplateComponent = DaggerKotlinBoilerplateComponent.builder()
                .kotlinBoilerplateModule(KotlinBoilerplateModule(applicationContext))
                .build()
    }
}