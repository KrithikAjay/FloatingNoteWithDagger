package com.krithik.floatingnote

import android.app.Application
import com.krithik.floatingnote.di.AppModule
import com.krithik.floatingnote.di.DaggerAppComponent
//import com.krithik.floatingnote.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector

import javax.inject.Inject


class MyApplication : Application()
        , HasAndroidInjector {


    @Inject
    lateinit var mInjector: DispatchingAndroidInjector<Any>
    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build().inject(this)


    }

    override fun androidInjector(): AndroidInjector<Any> {
        return mInjector
    }
}