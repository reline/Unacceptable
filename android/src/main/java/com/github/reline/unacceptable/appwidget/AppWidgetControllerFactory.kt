package com.github.reline.unacceptable.appwidget

interface AppWidgetControllerFactory {
    fun getAppWidgetController(appWidgetId: Int): AppWidgetController
}
