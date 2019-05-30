package com.example.test.Fragment

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import com.example.test.AdapterPhotos
import com.example.test.Database.DBPhotos
import com.example.test.Database.Photo
import com.example.test.Fragment.Photo.Loader.PhotoApi
import com.example.test.Fragment.Photo.Loader.RawPhoto
import com.example.test.MainActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import com.example.test.R

class PhotoFragment : Fragment() {
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    // Таргеты подвержены нападению сборщика
    var targets = ArrayList<MyTarget>()
    var countLoaded = 0

    private val api by lazy {
        PhotoApi.create()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_photo, container, false)

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh)
        swipeRefreshLayout.setOnRefreshListener {
            api.search(50)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { result ->
                                MainActivity.db.dropPhotos()
                                loadToDatabase(result)
                            },
                            { error ->
                                Toast.makeText(context, error.message + " ", Toast.LENGTH_LONG).show()
                                swipeRefreshLayout.isRefreshing = false
                            }
                    )
        }

        return view
    }

    fun loadToListView() {
        val listView = view?.findViewById<ListView>(R.id.list_photos)
        val arrAlarm = MainActivity.db.all(0)
        val adapter = AdapterPhotos(context, arrAlarm)
        listView?.adapter = adapter
    }

    private fun loadToDatabase(arr: List<RawPhoto>) {
        arr.forEach { photo ->
            val target = MyTarget(photo, arr.size)
            targets.add(target)

            Picasso.with(activity)
                    .load(photo.urls.small)
                    .skipMemoryCache()
                    .into(target)
        }
    }

    inner class MyTarget(private val photo: RawPhoto, private val count: Int) : Target {
        override fun onPrepareLoad(p0: Drawable?) {}

        override fun onBitmapFailed(p0: Drawable?) {
            countLoaded++
        }

        override fun onBitmapLoaded(p0: Bitmap?, p1: Picasso.LoadedFrom?) {
            countLoaded++
            if (p0 !== null) {
                MainActivity.db.insert(Photo(photo.id, p0, photo.width, photo.height, 0))
            }

            if (countLoaded >= count) {
                targets = ArrayList()
                swipeRefreshLayout.isRefreshing = false
            }

            loadToListView()
        }
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.onCreate(savedInstanceState)

        activity.title = resources.getString(R.string.photo_fragment_title)

        loadToListView()
    }
}


