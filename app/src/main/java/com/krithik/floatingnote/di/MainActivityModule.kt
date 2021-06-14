package com.krithik.floatingnote.di

import com.krithik.floatingnote.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

//For Dagger
@Module
abstract class MainActivityModule {

    @ContributesAndroidInjector
    abstract fun contributeMainActivityInjector(): MainActivity


}