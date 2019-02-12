package com.example.test.Music

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.test.AppPreferences
import com.example.test.DataBase.Sound
import com.example.test.MainActivity
import com.example.test.R
import java.util.*

class AdapterMusic(val context: Context, val data: ArrayList<Sound>, val classMusic: ClassMusic, val startId: Int) : BaseAdapter() {
    var mInflater: LayoutInflater

    init {
        mInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rawView = mInflater.inflate(R.layout.list_music, parent, false)
        val textV1 = rawView.findViewById<TextView>(R.id.MusicListView1)
        val textV2 = rawView.findViewById<TextView>(R.id.MusicListView2)
        val textV3 = rawView.findViewById<TextView>(R.id.MusicListView3)

        val item = getItem(position)

        textV1.text = (startId + position).toString()

        textV2.setOnClickListener { _ ->
            if (MainActivity.fragIndexSecond == 0) {
                AppPreferences.lastMusic = item.id
                classMusic.startSound()
            }
        }

        textV2.text = item.name
        textV3.text = item.duration

        return rawView
    }

    override fun getItem(position: Int): Sound {
        return data[position]
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    override fun getCount(): Int {
        return data.size
    }
}