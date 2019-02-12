package com.example.test.Music

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import com.example.test.AppPreferences
import com.example.test.DataBase.Sound
import com.example.test.MainActivity
import com.example.test.R
import java.util.ArrayList


class AdapterMusicFolder(val context: Context, val data: ArrayList<Sound>, val classMusic: ClassMusic) : BaseAdapter() {
    private var mInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private val arr = arrayListOf<Folder>()

    init {
        val map = hashMapOf<String, Int>()

        for (v in data) {
            val second = map[v.directory]
            if (second == null) {
                map[v.directory] = arrayListOf(v)
            } else {
                second.add(v)
                map[v.directory] = second
            }
        }
        var count = 1
        for ((title, values) in map) {
            arr.add(Folder(count, title, values))
            count += values.size
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rawView = mInflater.inflate(R.layout.list_music_folder, parent, false)
        val title = rawView.findViewById<TextView>(R.id.music_title)
        val content = rawView.findViewById<ListView>(R.id.music_folder)

        val item = getItem(position)
        title.text = item.title
        content.adapter = AdapterMusic(context, item.arr, classMusic, item.count)

        return rawView
    }

    override fun getItem(position: Int): Folder {
        return arr[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return arr.size
    }

    class Folder(val count: Int, val title: String, val arr: ArrayList<Sound>)
}