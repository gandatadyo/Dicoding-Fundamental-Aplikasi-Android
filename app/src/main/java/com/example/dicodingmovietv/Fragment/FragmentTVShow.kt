package com.example.dicodingmovietv.Fragment


import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingmovietv.Adapter.AdapterRecyclerTVShow
import com.example.dicodingmovietv.Model.ParcelableData
import com.example.dicodingmovietv.R
import com.example.dicodingmovietv.ReminderActivity
import com.example.dicodingmovietv.RestApi.RestAPITVShow
import kotlinx.android.synthetic.main.fragment_tvshow.view.*

class FragmentTVShow : Fragment() {
    // Fragment with Recyclerview
    private var dataTV = arrayListOf<ParcelableData>()
    private val cardViewHeroAdapter = AdapterRecyclerTVShow(dataTV)
    private var progressdlg : ProgressBar? = null
    lateinit var mainViewModel: RestAPITVShow
    val ACTION_LOCALE = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_tvshow, container, false)
        val toolbar = rootView.toolbarTVShow

        toolbar?.inflateMenu(R.menu.toolbar_menu)
        toolbar?.title = "Dicoding Movie"
        val searchView = toolbar.menu.findItem(R.id.search).actionView as SearchView
        searchView.queryHint = resources.getString(R.string.search)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                mainViewModel.CallData(requireContext(),query)
                mainViewModel.GetData().observe(requireActivity(), Observer { dataItems ->
                    if (dataItems != null) {
                        dataTV.clear()
                        dataTV.addAll(dataItems)
                        cardViewHeroAdapter.notifyDataSetChanged()
                        progressdlg?.visibility = View.GONE
                    }
                })
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        toolbar.menu.findItem(R.id.action_change_settings).setOnMenuItemClickListener {
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivityForResult(mIntent,ACTION_LOCALE)
            true
        }

        toolbar.menu.findItem(R.id.action_reminder_settings).setOnMenuItemClickListener {
            startActivity(Intent(requireContext(), ReminderActivity::class.java))
            true
        }
        progressdlg = rootView.progressBarTVShow

        rootView.recylerViewTVShow.setHasFixedSize(true)
        rootView.recylerViewTVShow.layoutManager = LinearLayoutManager(requireContext())
        rootView.recylerViewTVShow.adapter = cardViewHeroAdapter

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(RestAPITVShow::class.java)
        mainViewModel.CallData(requireContext(),"")
        mainViewModel.GetData().observe(this, Observer { dataItems ->
            if (dataItems != null) {
                dataTV.clear()
                dataTV.addAll(dataItems)
                cardViewHeroAdapter.notifyDataSetChanged()
                progressdlg?.visibility = View.GONE
            }
        })
        return rootView
    }
}
