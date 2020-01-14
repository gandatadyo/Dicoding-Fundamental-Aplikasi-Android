package com.example.dicodingmovietv.Database

import android.content.ContentValues
import android.content.Context
import android.content.res.Resources
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.dicodingmovietv.Database.DatabaseContract.FavoriteColumns.Companion.TABLE_NAMEMovie
import com.example.dicodingmovietv.Database.DatabaseContract.FavoriteColumns.Companion.TABLE_NAMETV
import com.example.dicodingmovietv.Database.DatabaseContract.FavoriteColumns.Companion.TITLE
import com.example.dicodingmovietv.Database.DatabaseContract.FavoriteColumns.Companion._ID
import com.example.dicodingmovietv.R
import java.sql.SQLException

class FavoriteHelper (context: Context) {
    private val dataBaseHelper: DatabaseHelper = DatabaseHelper(context)
    private lateinit var database: SQLiteDatabase

    companion object {
        private const val DATABASE_TABLEMovie = TABLE_NAMEMovie
        private const val DATABASE_TABLETV = TABLE_NAMETV
        private var INSTANCE: FavoriteHelper? = null

        fun getInstance(context: Context): FavoriteHelper {
            if (INSTANCE == null) {
                synchronized(SQLiteOpenHelper::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = FavoriteHelper(context)
                    }
                }
            }
            return INSTANCE as FavoriteHelper
        }
    }

    @Throws(SQLException::class)
    fun open() {
        database = dataBaseHelper.writableDatabase
    }

    fun close() {
        dataBaseHelper.close()
        if (database.isOpen)
            database.close()
    }

    fun queryAllMovie(): Cursor {
        return database.query(DATABASE_TABLEMovie,null,null,null,null,null,"$_ID ASC",null)
    }
    fun queryAllTv(): Cursor {
        return database.query(DATABASE_TABLETV,null,null,null,null,null,"$_ID ASC",null)
    }

    fun insertMovie(values: ContentValues?,context: Context): String {
        var sresult: String
        val cursor:Cursor =  database.query(DATABASE_TABLEMovie, null, "$TITLE = ?", arrayOf(values?.getAsString(TITLE)), null, null, null, null)
        sresult = if(cursor.count==0){
            val result:Long = database.insert(DATABASE_TABLEMovie, null, values)
            if(result>0) context.getString(R.string.successfulysaved) else context.getString(R.string.failedtosaved)
        }else context.getString(R.string.dataalreadyexist)
        return sresult
    }

    fun insertTv(values: ContentValues?,context: Context): String {
        var sresult: String
        val cursor:Cursor =  database.query(DATABASE_TABLETV, null, "$TITLE = ?", arrayOf(values?.getAsString(TITLE)), null, null, null, null)
        sresult = if(cursor.count==0){
            val result:Long = database.insert(DATABASE_TABLETV, null, values)
            if(result>0) context.getString(R.string.successfulysaved) else context.getString(R.string.failedtosaved)
        }else context.getString(R.string.dataalreadyexist)
        return sresult
    }

    fun queryCheckMovie(titledata: String): Boolean {
        val cursor:Cursor =  database.query(DATABASE_TABLEMovie, null, "$TITLE = ?", arrayOf(titledata), null, null, null, null)
        return cursor.count==0
    }

    fun queryCheckTVShow(titledata: String): Boolean {
        val cursor:Cursor =  database.query(DATABASE_TABLETV, null, "$TITLE = ?", arrayOf(titledata), null, null, null, null)
        return cursor.count==0
    }

    fun deleteByIdMovie(title: String): Int {
        return database.delete(DATABASE_TABLEMovie, "$TITLE = '$title'", null)
    }


    fun deleteByIdTv(title: String): Int {
        return database.delete(DATABASE_TABLETV, "$TITLE = '$title'", null)
    }
}