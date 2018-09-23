package com.github.reline.unacceptable.injection.components

import com.github.reline.unacceptable.injection.modules.ApplicationModule
import com.github.reline.unacceptable.injection.modules.BroadcastReceiverModule
import com.github.reline.unacceptable.UnacceptableApplication
import com.github.reline.unacceptable.injection.modules.ActivityModule
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidInjectionModule::class, ApplicationModule::class,
    BroadcastReceiverModule::class, ActivityModule::class])
interface ApplicationComponent {
    fun inject(application: UnacceptableApplication)
}