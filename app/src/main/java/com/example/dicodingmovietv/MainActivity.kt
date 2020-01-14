package com.example.dicodingmovietv

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.dicodingmovietv.Database.FavoriteHelper
import com.example.dicodingmovietv.Fragment.FragmentFavorite
import com.example.dicodingmovietv.Fragment.FragmentMovie
import com.example.dicodingmovietv.Fragment.FragmentTVShow
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var favHelper: FavoriteHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.supportActionBar?.hide()
        supportActionBar?.elevation = 0f // for remove shadow below toolbar

        bottomnav.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        val fragment = FragmentMovie()
        addFragment(fragment)

        favHelper = FavoriteHelper.getInstance(applicationContext)
        favHelper.open()
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.menu_movie -> {
                val fragment = FragmentMovie()
                addFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.menu_tvshow -> {
                val fragment = FragmentTVShow()
                addFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.menu_fav -> {
                val fragment = FragmentFavorite()
                addFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun addFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.design_bottom_sheet_slide_in, R.anim.design_bottom_sheet_slide_out)
            .replace(R.id.framelayout, fragment, fragment.javaClass.getSimpleName())
            .commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        favHelper.close()
    }
}

