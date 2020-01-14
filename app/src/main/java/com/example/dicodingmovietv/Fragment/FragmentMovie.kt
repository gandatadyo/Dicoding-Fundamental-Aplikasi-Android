package com.example.dicodingmovietv.Fragment


import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ProgressBar
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.dicodingmovietv.Adapter.AdapterListMovie
import com.example.dicodingmovietv.DetailActivity
import com.example.dicodingmovietv.Model.ParcelableData
import com.example.dicodingmovietv.ReminderActivity
import com.example.dicodingmovietv.RestApi.RestAPIMovie
import com.example.dicodingmovietv.R
import kotlinx.android.synthetic.main.fragment_movie.view.*

class FragmentMovie : Fragment() {
    // Fragment with ListView
    private lateinit var adapterMovie: AdapterListMovie
    private var dataMovie= arrayListOf<ParcelableData>()
    private var progressdlg : ProgressBar? = null
    private lateinit var mainViewModel: RestAPIMovie
    val ACTION_LOCALE = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_movie, container, false)
        val toolbar = rootView.toolbarMovie

        toolbar?.inflateMenu(R.menu.toolbar_menu)
        toolbar?.title = "Dicoding Movie"
        val searchView = toolbar.menu.findItem(R.id.search).actionView as SearchView
        searchView.queryHint = resources.getString(R.string.search)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                mainViewModel.CallData(requireContext(),query)
                mainViewModel.GetData().observe(requireActivity(), Observer { dataItems ->
                    if (dataItems != null) {
                        dataMovie.clear()
                        dataMovie.addAll(dataItems)
                        adapterMovie.notifyDataSetChanged()
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
        progressdlg = rootView.progressBarMovie

        adapterMovie = AdapterListMovie(requireContext())
        adapterMovie.movies = dataMovie
        rootView.listFilm.adapter = adapterMovie
        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(RestAPIMovie::class.java)
        mainViewModel.CallData(requireContext(),"")
        mainViewModel.GetData().observe(this, Observer { dataItems ->
            if (dataItems != null) {
                dataMovie.clear()
                dataMovie.addAll(dataItems)
                adapterMovie.notifyDataSetChanged()
                progressdlg?.visibility = View.GONE
            }
        })

        rootView.listFilm.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val objIntent = Intent(requireContext(), DetailActivity::class.java)
            objIntent.putExtra("mode","movie")
            objIntent.putExtra(DetailActivity.EXTRA_PERSON, dataMovie[position])
            startActivity(objIntent)
        }
        return rootView
    }
}
