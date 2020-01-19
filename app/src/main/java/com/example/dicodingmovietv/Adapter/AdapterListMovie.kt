package com.example.dicodingmovietv.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.dicodingmovietv.Model.ParcelableData
import com.example.dicodingmovietv.R
import com.squareup.picasso.Picasso

class AdapterListMovie internal constructor(private val context: Context) : BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var itemView = convertView
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.item_view, parent, false)
        }
        val viewHolder = ViewHolder(itemView as View)
        val hero = getItem(position) as ParcelableData
        viewHolder.bind(hero)
        return itemView
    }

    override fun getItem(position: Int): Any {
        return movies[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return movies.size
    }

    private inner class ViewHolder internal constructor(view: View) {
        private val txtName: TextView = view.findViewById(R.id.txt_name)
        private val txtDescription: TextView = view.findViewById(R.id.txt_description)
        private val imgPhoto: ImageView = view.findViewById(R.id.img_photo)
        internal fun bind(movie: ParcelableData) {
            txtName.text = movie.name
            txtDescription.text = movie.desc
            val imgurlsmall = "https://image.tmdb.org/t/p/w92/${movie.img}"
            Picasso.get().load(imgurlsmall).into(imgPhoto)
        }
    }

    internal var movies = arrayListOf<ParcelableData>()
}