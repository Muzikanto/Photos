package com.example.test

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.example.test.Database.Photo
import com.example.test.R

class AdapterPhotos(val context: Context, val arr: ArrayList<Photo>) : BaseAdapter() {
    var mInflater: LayoutInflater

    init {
        mInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        Log.d("test_log", arr.size.toString())
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rawView = mInflater.inflate(R.layout.list_photos, parent, false)
        val image = rawView.findViewById<ImageView>(R.id.list_photos_image)
        image.setImageBitmap(arr[position].bitmap)

        return rawView
    }

    override fun getItem(position: Int): Any {
        return arr[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return arr.size
    }
}