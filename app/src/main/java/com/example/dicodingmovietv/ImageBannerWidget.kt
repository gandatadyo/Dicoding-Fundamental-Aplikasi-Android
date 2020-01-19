package com.example.dicodingmovietv

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.core.net.toUri

class ImageBannerWidget : AppWidgetProvider() {
    private val WIDGET_RELOAD = "widgetsreload"
    private val WIDGET_ID_EXTRA = "widget_id_extra"

    override fun onUpdate(context: Context,appWidgetManager: AppWidgetManager,appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.getStringExtra("WIDGET_RELOAD")==WIDGET_RELOAD) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val appWidgetId = intent.getIntExtra(WIDGET_ID_EXTRA, 0)

            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    private fun getPendingSelfIntent(context: Context, appWidgetId: Int): PendingIntent {
        val intent = Intent(context, javaClass)
        intent.putExtra("WIDGET_RELOAD", WIDGET_RELOAD)
        return PendingIntent.getBroadcast(context, appWidgetId, intent, 0)
    }

    private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        val views = RemoteViews(context.packageName, R.layout.image_banner_widget)

        val intent = Intent(context, StackWidgetService::class.java)
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        intent.data = intent.toUri(Intent.URI_INTENT_SCHEME).toUri()
        views.setRemoteAdapter(R.id.stack_view, intent)

        val widgetText = context.getString(R.string.appwidget_text)
        views.setTextViewText(R.id.appwidget_text, widgetText)
        views.setOnClickPendingIntent(R.id.btnOk_Widget, getPendingSelfIntent(context, appWidgetId))

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}