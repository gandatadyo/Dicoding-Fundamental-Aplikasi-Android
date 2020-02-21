package com.example.dicodingmovietv.Widget

import android.content.Intent
import android.widget.RemoteViewsService
import com.example.dicodingmovietv.Widget.StackRemoteViewsFactory

class StackWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory = StackRemoteViewsFactory(this.applicationContext)
}