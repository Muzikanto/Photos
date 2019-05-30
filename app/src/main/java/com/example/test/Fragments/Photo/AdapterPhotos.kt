package com.example.test

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.example.test.Database.Photo

class AdapterPhotos(private val context: Context, private val arr: ArrayList<Photo>) : BaseAdapter() {
    private var mInflater: LayoutInflater

    init {
        mInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rawView = mInflater.inflate(R.layout.list_photos, parent, false)
        val image = rawView.findViewById<ImageView>(R.id.list_photos_image)
        image.setImageBitmap(arr[position].bitmap)

        rawView.setOnClickListener {
            val i = Intent(context, PhotoActivity::class.java)
            i.putExtra("id", arr[position].id)
            context.startActivity(i)
        }

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