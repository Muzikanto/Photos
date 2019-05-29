package com.example.test

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.example.test.Database.DBPhotos
import com.example.test.Database.Photo
import com.example.test.Fragment.Photo.Loader.PhotoApi
import com.example.test.Fragment.Photo.Loader.RawPhoto
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target

class PhotoFragment : Fragment() {
    lateinit var db: DBPhotos
    // Таргеты подвержены нападению сборщика
    var targets = ArrayList<MyTarget>()

    private val api by lazy {
        PhotoApi.create()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context !== null) {
            db = DBPhotos(context)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_photo, container, false)
        loadToListView()

        api.search(50)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            loadToDatabase(result)
                        },
                        { error ->
                            Log.d("test_log _error", error.message + " ")
                        }
                )

        return view
    }

    fun loadToListView() {
        val listView = view?.findViewById<ListView>(R.id.list_photos)
        val arrAlarm = db.all()
        val adapter = AdapterPhotos(context, arrAlarm)
        listView?.adapter = adapter
    }

    private fun loadToDatabase(arr: List<RawPhoto>) {
        db.drop()

        arr.forEach { photo ->
            val target = MyTarget(photo)
            targets.add(target)
            Picasso.with(activity)
                    .load(photo.urls.small)
                    .skipMemoryCache()
                    .into(target)
        }
    }

    inner class MyTarget(val photo: RawPhoto) : Target {
        override fun onPrepareLoad(p0: Drawable?) {}

        override fun onBitmapFailed(p0: Drawable?) {}

        override fun onBitmapLoaded(p0: Bitmap?, p1: Picasso.LoadedFrom?) {
            if (p0 !== null) {
                db.insert(Photo(photo.id, p0, photo.width, photo.height))
            }

            loadToListView()

            if (targets.size >= 50) {
                targets = ArrayList()
            }
        }
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.onCreate(savedInstanceState)

        activity.title = resources.getString(R.string.photo_fragment_title)
    }
}


