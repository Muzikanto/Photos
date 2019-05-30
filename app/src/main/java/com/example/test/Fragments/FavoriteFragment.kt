package com.example.test.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.widget.ListView
import com.example.test.AdapterPhotos
import com.example.test.MainActivity
import com.example.test.R

class FavoriteFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_favorite, container, false)

        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.onCreate(savedInstanceState)
        activity.title = resources.getString(R.string.favorite_fragment_title)

        loadToListView()
    }

    private fun loadToListView() {
        val listView = view?.findViewById<ListView>(R.id.list_favorites)
        val arr = MainActivity.db.all(1)
        val adapter = AdapterPhotos(context, arr)
        listView?.adapter = adapter
    }

    override fun onResume() {
        super.onResume()

        loadToListView()
    }
}



