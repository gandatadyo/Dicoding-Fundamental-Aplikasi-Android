package com.example.dicodingmovietv.Fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.dicodingmovietv.FragmentAdapter.PageAdapterFavorite

import com.example.dicodingmovietv.R
import kotlinx.android.synthetic.main.fragment_favorite.view.*

class FragmentFavorite : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_favorite, container, false)

        val sectionsPagerAdapter = PageAdapterFavorite(requireContext(), childFragmentManager)
        rootView.view_pager_favorite.adapter = sectionsPagerAdapter
        rootView.tabs_favorite?.setupWithViewPager(rootView.view_pager_favorite.view_pager_favorite)
        return rootView
    }
}
