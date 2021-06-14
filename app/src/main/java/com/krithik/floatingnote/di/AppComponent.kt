package com.krithik.floatingnote.di


import com.krithik.floatingnote.MyApplication
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
        modules = [
            AndroidInjectionModule::class,
            AppModule::class,
            MainActivityModule::class
        ]
)
interface AppComponent {

    fun inject(application: MyApplication)
}