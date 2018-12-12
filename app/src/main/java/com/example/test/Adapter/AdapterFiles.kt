package com.example.test.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.test.DataBase.ListFiles
import com.example.test.R
import java.util.ArrayList



class AdapterFiles(val context: Context, val data: ArrayList<ListFiles>) : BaseAdapter() {
    var mInflater: LayoutInflater

    init {
        mInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rawView = mInflater.inflate(R.layout.list_files, parent, false)
        val textV1 = rawView.findViewById<TextView>(R.id.FileListType)
        val textV2 = rawView.findViewById<TextView>(R.id.FileListName)
        if (data[position].isDirectory)
            textV1.setText(context.resources.getString(R.string.filesSearchD))
        else
            textV1.setText(context.resources.getString(R.string.filesSearchF))
        textV2.setText(data[position].name)
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