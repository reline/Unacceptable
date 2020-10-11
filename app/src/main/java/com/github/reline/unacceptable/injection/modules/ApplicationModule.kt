package com.github.reline.unacceptable.injection.modules

import android.app.Application
import android.content.Context
import android.os.Vibrator
import android.view.WindowManager
import com.github.reline.unacceptable.appwidget.AppWidgetControllerFactory
import com.github.reline.unacceptable.appwidget.DefaultAppWidgetControllerFactory
import com.github.reline.unacceptable.mediaplayer.DefaultMediaPlayerFactory
import com.github.reline.unacceptable.mediaplayer.MediaPlayerFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object ApplicationModule {
    @Provides
    @Singleton
    fun provideAppWidgetControllerFactory(application: Application, mediaPlayerFactory: MediaPlayerFactory, vibrator: Vibrator, windowManager: WindowManager): AppWidgetControllerFactory {
        return DefaultAppWidgetControllerFactory(application, mediaPlayerFactory, vibrator, windowManager)
    }

    @Provides
    @Singleton
    fun provideMediaPlayerFactory(application: Application): MediaPlayerFactory {
        return DefaultMediaPlayerFactory(application)
    }

    @Provides
    @Singleton
    fun provideVibrator(application: Application): Vibrator {
        return application.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    @Provides
    @Singleton
    fun provideWindowManager(application: Application): WindowManager {
        return application.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }
}
