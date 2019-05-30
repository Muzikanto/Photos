package com.example.test.Fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.test.MapsActivity
import com.example.test.R

class InfoFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_info, container, false)

        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.onCreate(savedInstanceState)
        activity.title = resources.getString(R.string.info_fragment_title)

        view?.findViewById<Button>(R.id.btn_map)?.setOnClickListener {
            val i = Intent(this@InfoFragment.context, MapsActivity::class.java)
            startActivity(i)
        }
    }
}



