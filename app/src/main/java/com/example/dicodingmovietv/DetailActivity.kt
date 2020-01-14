package com.example.dicodingmovietv

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.dicodingmovietv.Database.DatabaseContract
import com.example.dicodingmovietv.Database.FavoriteHelper
import com.example.dicodingmovietv.Model.ParcelableData
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
        Picasso.get().load(myData.img).into(imgDetail)
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
                SycnDataWidget(this)
                finish()
                return true
            }
            R.id.action_remove ->{
                finish()
                RemoveFavorite()
                SycnDataWidget(this)
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
    }

    private fun RemoveFavorite() {
        val result: Int? = if(intent.getStringExtra("mode")=="movie" ||intent.getStringExtra("mode")=="moviefav") favHelper.deleteByIdMovie(titledata)
        else favHelper.deleteByIdTv(titledata)

        if (result != null) {
            if (result> 0)  Toast.makeText(this, getString(R.string.datadelete), Toast.LENGTH_SHORT).show()
            else Toast.makeText(this, getString(R.string.faileddelete), Toast.LENGTH_SHORT).show()
        }
    }

    private fun CheckData():Boolean{
        var result = false
        if(intent.getStringExtra("mode")=="movie" ||intent.getStringExtra("mode")=="moviefav")  result = favHelper.queryCheckMovie(titledata)
        else if(intent.getStringExtra("mode")=="tvshow") result = favHelper.queryCheckTVShow(titledata)
        return result
    }

    fun SycnDataWidget(context: Context){
        val intent = Intent(context, ImagesBannerWidget::class.java)
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        val ids = AppWidgetManager.getInstance(context).getAppWidgetIds(ComponentName(context,ImagesBannerWidget::class.java!!))
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
        context.sendBroadcast(intent)
    }
}
