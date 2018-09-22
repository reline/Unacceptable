package com.github.reline.unacceptable

import android.app.Application
import android.content.BroadcastReceiver
import com.github.reline.unacceptable.injection.modules.ApplicationModule
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasBroadcastReceiverInjector
import javax.inject.Inject

class UnacceptableApplication : Application(), HasBroadcastReceiverInjector {

    @Inject
    lateinit var broadcastReceiverInjector: DispatchingAndroidInjector<BroadcastReceiver>

    override fun onCreate() {
        super.onCreate()
        DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .build()
                .inject(this);
    }

    override fun broadcastReceiverInjector() = broadcastReceiverInjector
}