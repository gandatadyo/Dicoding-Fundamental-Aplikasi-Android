package com.example.dicodingmovietv.Fragment


import android.database.Cursor
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingmovietv.Adapter.AdapterRecyclerTVShowFav
import com.example.dicodingmovietv.Database.DatabaseContract
import com.example.dicodingmovietv.Database.FavoriteHelper
import com.example.dicodingmovietv.Model.ParcelableData

import com.example.dicodingmovietv.R
import kotlinx.android.synthetic.main.fragment_fragment_favorite_tvshow.view.*

class FragmentFavoriteTVShow : Fragment() {
    private var dataTV = arrayListOf<ParcelableData>()
    private val cardViewHeroAdapter = AdapterRecyclerTVShowFav(dataTV)
    private lateinit var favHelper: FavoriteHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_fragment_favorite_tvshow, container, false)

        rootView.recylerViewFavTVShow.setHasFixedSize(true)
        rootView.recylerViewFavTVShow.layoutManager = LinearLayoutManager(requireContext())
        rootView.recylerViewFavTVShow.adapter = cardViewHeroAdapter

        favHelper = FavoriteHelper.getInstance(requireContext())
        return rootView
    }

    fun mapCursorToArrayList(notesCursor: Cursor){
        dataTV.clear()
        notesCursor.moveToFirst()
        for (x in 0 until notesCursor.count) {
            val hero = ParcelableData(
                notesCursor.getInt(notesCursor.getColumnIndexOrThrow(DatabaseContract.FavoriteColumns._ID)),
                notesCursor.getString(notesCursor.getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.TITLE)),
                notesCursor.getString(notesCursor.getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.DESCRIPTION)),
                notesCursor.getString(notesCursor.getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.IMG))
            )
            dataTV.add(hero)
            notesCursor.moveToNext()
        }
    }

    override fun onResume() {
        super.onResume()
        val cursor = favHelper.queryAllTv()
        mapCursorToArrayList(cursor)
        cardViewHeroAdapter.notifyDataSetChanged()
    }
}
