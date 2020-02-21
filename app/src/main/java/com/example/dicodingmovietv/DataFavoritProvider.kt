package com.example.dicodingmovietv

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.example.dicodingmovietv.Database.DatabaseContract
import com.example.dicodingmovietv.Database.FavoriteHelper

class DataFavoritProvider : ContentProvider() {

    companion object {

        private const val NOTE = 1
        private const val NOTE_ID = 2
        private lateinit var favoriteHelper: FavoriteHelper
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        val TABLE_NAME = DatabaseContract.FavoriteColumns.TABLE_NAMEMovie

        init {
            sUriMatcher.addURI("com.example.dicodingmovietv",TABLE_NAME, NOTE)
            sUriMatcher.addURI("com.example.dicodingmovietv", "$TABLE_NAME/#", NOTE_ID)
        }
    }

    override fun onCreate(): Boolean {
        favoriteHelper = FavoriteHelper.getInstance(context as Context)
        favoriteHelper.open()
        return true
    }

    override fun query( uri: Uri, projection: Array<String>?, selection: String?,selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        val cursor: Cursor?
        when (sUriMatcher.match(uri)) {
            NOTE -> cursor = favoriteHelper.queryAll()
            NOTE_ID -> cursor = favoriteHelper.queryById(uri.lastPathSegment.toString())
            else -> cursor = null
        }
        return cursor
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added: Long = when (NOTE) {
            sUriMatcher.match(uri) -> favoriteHelper.insert(values)
            else -> 0
        }
        val CONTENT_URI: Uri = Uri.Builder().scheme("content").authority("com.example.dicodingmovietv").appendPath(TABLE_NAME).build()
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?,selectionArgs: Array<String>?): Int {
        val updated: Int = when (NOTE_ID) {
            sUriMatcher.match(uri) -> favoriteHelper.update(uri.lastPathSegment.toString(),values)
            else -> 0
        }
        val CONTENT_URI: Uri = Uri.Builder().scheme("content").authority("com.example.dicodingmovietv").appendPath(TABLE_NAME).build()
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return updated
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val deleted: Int = when (NOTE_ID) {
            sUriMatcher.match(uri) -> favoriteHelper.deleteById(uri.lastPathSegment.toString())
            else -> 0
        }
        val CONTENT_URI: Uri = Uri.Builder().scheme("content").authority("com.example.dicodingmovietv").appendPath(TABLE_NAME).build()
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return deleted
    }
}
