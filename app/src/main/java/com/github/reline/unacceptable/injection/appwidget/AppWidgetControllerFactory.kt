package com.github.reline.unacceptable.injection.appwidget

interface AppWidgetControllerFactory {
    fun getAppWidgetController(appWidgetId: Int): AppWidgetController
}
