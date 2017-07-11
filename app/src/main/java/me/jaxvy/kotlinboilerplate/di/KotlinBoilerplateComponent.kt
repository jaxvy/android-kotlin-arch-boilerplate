package me.jaxvy.kotlinboilerplate.di

import dagger.Component
import me.jaxvy.kotlinboilerplate.ui.common.BaseActivity
import me.jaxvy.kotlinboilerplate.ui.common.BaseViewModel
import me.jaxvy.kotlinboilerplate.api.request.InjectionBase
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(KotlinBoilerplateModule::class))
interface KotlinBoilerplateComponent {

    fun inject(injectionBase: InjectionBase)

    fun inject(baseViewModel: BaseViewModel)
}