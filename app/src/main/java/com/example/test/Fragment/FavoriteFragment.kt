package com.example.test.Fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
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
    }
}



