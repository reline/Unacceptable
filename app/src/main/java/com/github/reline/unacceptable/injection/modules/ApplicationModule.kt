package com.github.reline.unacceptable.injection.modules

import android.app.Application
import android.content.Context
import android.os.Vibrator
import android.view.WindowManager
import com.github.reline.unacceptable.injection.appwidget.AppWidgetControllerFactory
import com.github.reline.unacceptable.injection.appwidget.DefaultAppWidgetControllerFactory
import com.github.reline.unacceptable.injection.mediaplayer.DefaultMediaPlayerFactory
import com.github.reline.unacceptable.injection.mediaplayer.MediaPlayerFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(private val application: Application) {
    @Provides
    @Singleton
    fun provideAppWidgetControllerFactory(mediaPlayerFactory: MediaPlayerFactory, vibrator: Vibrator, windowManager: WindowManager): AppWidgetControllerFactory {
        return DefaultAppWidgetControllerFactory(application, mediaPlayerFactory, vibrator, windowManager)
    }

    @Provides
    @Singleton
    fun provideMediaPlayerFactory(): MediaPlayerFactory {
        return DefaultMediaPlayerFactory(application)
    }

    @Provides
    @Singleton
    fun provideVibrator(): Vibrator {
        return application.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    @Provides
    @Singleton
    fun provideWindowManager(): WindowManager {
        return application.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }
}
