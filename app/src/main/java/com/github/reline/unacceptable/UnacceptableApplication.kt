package com.github.reline.unacceptable

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class UnacceptableApplication : Application() {
	@Inject
	lateinit var tree: Timber.Tree

	override fun onCreate() {
		super.onCreate()
		Timber.plant(tree)
	}
}
