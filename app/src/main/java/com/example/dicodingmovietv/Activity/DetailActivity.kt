package com.example.dicodingmovietv.Activity

import android.app.PendingIntent
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dicodingmovietv.Database.DatabaseContract
import com.example.dicodingmovietv.Database.FavoriteHelper
import com.example.dicodingmovietv.Model.ParcelableData
import com.example.dicodingmovietv.R
import com.example.dicodingmovietv.StackWidgetService
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*


class DetailActivity : AppCompatActivity() {
    private lateinit var favHelper: FavoriteHelper
    private var imgurl:String = ""
    private var iddata:String = ""
    private var titledata:String = ""
    companion object {
        const val EXTRA_PERSON = "DataDetailActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        favHelper = FavoriteHelper.getInstance(this)

        val myData = intent.getParcelableExtra(EXTRA_PERSON) as ParcelableData
        titleDetail.text = myData.name
        descDetail.text = myData.desc
        iddata = myData.id.toString()
        imgurl = myData.img.toString()
        titledata = myData.name.toString()
        val imgurllarge = "https://image.tmdb.org/t/p/w500/${imgurl}"
        Picasso.get().load(imgurllarge).into(imgDetail)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        if(CheckData()) {
            menu.findItem(R.id.action_favorite).isVisible = true
            menu.findItem(R.id.action_remove).isVisible = false

        }else{
            menu.findItem(R.id.action_favorite).isVisible = false
            menu.findItem(R.id.action_remove).isVisible = true
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.action_favorite ->{
                AddFavorite()
                finish()
                return true
            }
            R.id.action_remove ->{
                finish()
                RemoveFavorite()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun AddFavorite() {
        val values = ContentValues()
        values.put(DatabaseContract.FavoriteColumns.TITLE, titleDetail.text.toString())
        values.put(DatabaseContract.FavoriteColumns.DESCRIPTION, descDetail.text.toString())
        values.put(DatabaseContract.FavoriteColumns.IMG, imgurl)

        if(intent.getStringExtra("mode")=="movie"){
            val sresult = favHelper.insertMovie(values,this)
            Toast.makeText(this, sresult, Toast.LENGTH_SHORT).show()
        } else {
            val sresult = favHelper.insertTv(values,this)
            Toast.makeText(this, sresult, Toast.LENGTH_SHORT).show()
        }
        SyncDataWidget()
    }

    private fun RemoveFavorite() {
        val result: Int? = if(intent.getStringExtra("mode")=="movie" ||intent.getStringExtra("mode")=="moviefav") favHelper.deleteByIdMovie(titledata)
        else favHelper.deleteByIdTv(titledata)

        if (result != null) {
            if (result> 0)  Toast.makeText(this, getString(R.string.datadelete), Toast.LENGTH_SHORT).show()
            else Toast.makeText(this, getString(R.string.faileddelete), Toast.LENGTH_SHORT).show()
        }
        SyncDataWidget()
    }

    private fun CheckData():Boolean{
        var result = false
        if(intent.getStringExtra("mode")=="movie" ||intent.getStringExtra("mode")=="moviefav")  result = favHelper.queryCheckMovie(titledata)
        else if(intent.getStringExtra("mode")=="tvshow") result = favHelper.queryCheckTVShow(titledata)
        return result
    }

    private fun SyncDataWidget(){
        intent = Intent("android.appwidget.action.APPWIDGET_UPDATE")
        intent.putExtra("WIDGET_RELOAD","widgetsreload")
        sendBroadcast(intent)
    }
}
