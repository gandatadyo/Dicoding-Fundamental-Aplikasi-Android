package com.example.dicodingmovietv.Database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import com.example.dicodingmovietv.Database.DatabaseContract.*
import com.example.dicodingmovietv.Database.DatabaseContract.FavoriteColumns.Companion.TABLE_NAMEMovie
import com.example.dicodingmovietv.Database.DatabaseContract.FavoriteColumns.Companion.TABLE_NAMETV

internal class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "dbnoteapp"
        private const val DATABASE_VERSION = 1
        private val SQL_CREATE_TABLE_MOVIE = "CREATE TABLE $TABLE_NAMEMovie" +
                " (${FavoriteColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                " ${FavoriteColumns.TITLE} TEXT NOT NULL," +
                " ${FavoriteColumns.DESCRIPTION} TEXT NOT NULL," +
                " ${FavoriteColumns.IMG} TEXT NOT NULL);"
        private val SQL_CREATE_TABLE_TV = "CREATE TABLE $TABLE_NAMETV" +
                " (${FavoriteColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                " ${FavoriteColumns.TITLE} TEXT NOT NULL," +
                " ${FavoriteColumns.DESCRIPTION} TEXT NOT NULL," +
                " ${FavoriteColumns.IMG} TEXT NOT NULL);"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_MOVIE)
        db.execSQL(SQL_CREATE_TABLE_TV)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAMEMovie")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAMETV")
        onCreate(db)
    }
}

internal class DatabaseContract {

    internal class FavoriteColumns : BaseColumns {
        companion object {
            const val TABLE_NAMEMovie = "dbmfavoritemovie"
            const val TABLE_NAMETV = "dbmfavoritetv"
            const val _ID = "_id"
            const val TITLE = "title"
            const val DESCRIPTION = "description"
            const val IMG = "img"
        }
    }
}