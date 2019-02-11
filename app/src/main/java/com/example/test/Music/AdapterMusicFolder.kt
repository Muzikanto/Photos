package com.example.test.Music

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.test.DataBase.Sound
import com.example.test.R
import java.util.ArrayList


class AdapterMusicFolder(val context: Context, val data: ArrayList<Sound>) : BaseAdapter() {
    var mInflater: LayoutInflater
    val map = hashMapOf<String, ArrayList<Sound>>()

    init {
        mInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        for (v in data) {
            val second = map[v.directory]
            if (second == null) {
                map[v.directory] = arrayListOf(v)
            } else {
                second.add(v)
                map[v.directory] = second
            }
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rawView = mInflater.inflate(R.layout.list_music, parent, false)
        val textV1 = rawView.findViewById<TextView>(R.id.MusicListView1)
        val textV2 = rawView.findViewById<TextView>(R.id.MusicListView2)
        val textV3 = rawView.findViewById<TextView>(R.id.MusicListView3)
        textV1.setText((position + 1).toString())
        var name = data[position].name
        if (name.length >= 35)
            name = name.substring(0, 35) + ".."
        textV2.setText(name)
        textV3.setText(data[position].duration)
        return rawView
    }

    override fun getItem(position: Int): Any {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return data.size
    }
}