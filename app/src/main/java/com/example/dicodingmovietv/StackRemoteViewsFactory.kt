package com.example.dicodingmovietv

import android.app.Notification
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.example.dicodingmovietv.Database.DatabaseContract
import com.example.dicodingmovietv.Database.FavoriteHelper
import com.example.dicodingmovietv.Model.ParcelableData
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*
import java.util.*

/**
 * Created by dicoding on 1/9/2017.
 */

internal class StackRemoteViewsFactory(private val mContext: Context) : RemoteViewsService.RemoteViewsFactory {

    private lateinit var favHelper: FavoriteHelper
    private val mWidgetItemsUrl = ArrayList<String>()

    override fun onCreate() {

    }

    override fun onDataSetChanged() {
        // Untuk mengambil database dan tambahkan ke arraylist
        mWidgetItemsUrl.clear()
        favHelper = FavoriteHelper.getInstance(mContext)
        val cursor = favHelper.queryAllMovie()
        cursor.moveToFirst()
        for (x in 0 until cursor.count) {
            mWidgetItemsUrl.add( cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.IMG)))
            cursor.moveToNext()
        }
    }

    override fun onDestroy() {

    }

    override fun getCount(): Int = mWidgetItemsUrl.size

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(mContext.packageName, R.layout.widget_item)
        // Insert Bitmap
        var bitmap:Bitmap ?= null
        bitmap = Picasso.get().load(mWidgetItemsUrl[position]).get()
        rv.setImageViewBitmap(R.id.imageView, bitmap)
        val extras = bundleOf(
                ImagesBannerWidget.EXTRA_ITEM to position
        )

        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)

        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(i: Int): Long = 0

    override fun hasStableIds(): Boolean = false

}