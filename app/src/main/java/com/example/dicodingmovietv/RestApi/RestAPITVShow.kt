package com.example.dicodingmovietv.RestApi

import android.content.Context
import android.content.res.Resources
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.dicodingmovietv.Model.ParcelableData
import com.example.dicodingmovietv.R
import org.json.JSONArray
import org.json.JSONObject

class RestAPITVShow: ViewModel() {
    companion object {
        private const val API_KEY = "1c82c78abfacc7ee0508966c9489f84c"
    }
    private val listTVShow = MutableLiveData<ArrayList<ParcelableData>>()

    internal fun CallData(context: Context,search:String) {
        val queue = Volley.newRequestQueue(context)
        val url: String
        if(search=="") url = "https://api.themoviedb.org/3/discover/tv?api_key=${API_KEY}&language="+  context.getString(R.string.language)
        else url = "https://api.themoviedb.org/3/search/tv?api_key=${API_KEY}&language="+context.getString(R.string.language)+"&query="+search

        val postRequest = object :  StringRequest(
            Method.GET, url,
            Response.Listener<String> { response ->
                HandleResponse(response)
            }, Response.ErrorListener {
                Toast.makeText(context, Resources.getSystem().getString(R.string.somethingwrong), Toast.LENGTH_SHORT).show()
            }){}
        queue.add(postRequest)
    }

    private fun HandleResponse(result:String){
        val listItems = ArrayList<ParcelableData>()
        val responseObject = JSONObject(result)
        val list: JSONArray = responseObject.getJSONArray("results")

        for (i in 0 until list.length()) {
            val item = ParcelableData(
                0,
                list.getJSONObject(i).getString("original_name"),
                list.getJSONObject(i).getString("overview"),
                "https://image.tmdb.org/t/p/w92${list.getJSONObject(i).getString("poster_path")}"
            )
            listItems.add(item)
        }
        listTVShow.postValue(listItems)
    }

    internal fun GetData(): LiveData<ArrayList<ParcelableData>> {
        return listTVShow
    }
}