package com.github.reline.unacceptable.injection.modules

import com.github.reline.unacceptable.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeMainActivityInjector(): MainActivity
}