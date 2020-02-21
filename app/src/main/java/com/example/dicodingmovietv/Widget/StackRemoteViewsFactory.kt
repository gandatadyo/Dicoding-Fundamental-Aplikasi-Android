package com.example.dicodingmovietv.Widget

import android.content.Context
import android.graphics.Bitmap
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.example.dicodingmovietv.Database.DatabaseContract
import com.example.dicodingmovietv.Database.FavoriteHelper
import com.example.dicodingmovietv.R
import com.squareup.picasso.Picasso

internal class StackRemoteViewsFactory(private val mContext: Context) : RemoteViewsService.RemoteViewsFactory {
    private val mWidgetItemsUrl = ArrayList<String>()

    override fun onCreate() {
    }

    override fun onDataSetChanged() {
        val favHelper: FavoriteHelper = FavoriteHelper.getInstance(mContext)
        val cursor = favHelper.queryAllMovie()

        mWidgetItemsUrl.clear()
        if(cursor.count>0) {
            cursor.moveToFirst()
            for (x in 0 until cursor.count) {
                mWidgetItemsUrl.add(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.IMG)))
                cursor.moveToNext()
            }
        }
    }

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(mContext.packageName,R.layout.widget_item_views)

        if(mWidgetItemsUrl.size>0) {
            val bitmap: Bitmap
            val imglarge = "https://image.tmdb.org/t/p/w500/${mWidgetItemsUrl[position]}"
            bitmap = Picasso.get().load(imglarge).get()

            rv.setImageViewBitmap(R.id.img_widget, bitmap)
        }
        return rv
    }

    override fun getCount(): Int = mWidgetItemsUrl.size

    override fun onDestroy() {}

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(i: Int): Long = 0

    override fun hasStableIds(): Boolean = false
}