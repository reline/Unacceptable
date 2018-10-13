package com.github.reline.unacceptable.injection.appwidget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowManager
import android.widget.RemoteViews
import com.github.reline.unacceptable.R
import dagger.android.AndroidInjection
import javax.inject.Inject

class UnacceptableWidgetProvider : AppWidgetProvider() {

    @Inject
    lateinit var controllerFactory: AppWidgetControllerFactory

    override fun onEnabled(context: Context) {
        // AppWidget for this provider is instantiated
    }

    override fun onDisabled(context: Context) {
        // last AppWidget instance for this provider is deleted
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        // one or more AppWidget instances have been deleted
        // only one widget is ever given in the current implementation
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        appWidgetIds.forEach { appWidgetId ->
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onAppWidgetOptionsChanged(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int, newOptions: Bundle) {
        val maxHeight = newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT)
        val maxWidth = newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH)
        controllerFactory.getAppWidgetController(appWidgetId).onSizeChanged(maxWidth, maxHeight)
    }

    private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        // create the click listener
        val intent = Intent(context, UnacceptableWidgetProvider::class.java)
                .putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        val pendingIntent = PendingIntent.getBroadcast(context, appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        // set the click listener
        val views = RemoteViews(context.packageName, R.layout.unacceptable_layout)
        views.setOnClickPendingIntent(R.id.unacceptableButton, pendingIntent)
        // update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    private fun onLemonClicked(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        controllerFactory.getAppWidgetController(appWidgetId).onClick()
    }

    override fun onReceive(context: Context, intent: Intent) {
        AndroidInjection.inject(this, context)
        super.onReceive(context, intent)
        if (intent.action == null) {
            val widgetId = intent.extras?.get(AppWidgetManager.EXTRA_APPWIDGET_ID) as? Int ?: return
            onLemonClicked(context, AppWidgetManager.getInstance(context), widgetId)
        }
    }

}