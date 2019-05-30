package com.example.test

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.test.Database.Photo
import com.example.test.Fragments.Photo.Loader.PhotoApi
import com.example.test.Fragments.Photo.Loader.RawPhoto
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class PhotoActivity : AppCompatActivity() {
    private var rawPhoto: RawPhoto? = null
    private lateinit var photo: Photo

    private val api by lazy {
        PhotoApi.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.photo)

        val imageView = findViewById<ImageView>(R.id.sinlge_image)

        val photoId = intent.getStringExtra("id")
        photo = MainActivity.db.find(photoId)
        imageView.setImageBitmap(photo.bitmap)

        clickAction(findViewById(R.id.single_btn_favorite), photoId, photo.favorite)
        loadData(photoId)
    }


    private fun clickAction(btnFavorite: Button, id: String, favorite: Int) {
        btnFavorite.setOnClickListener {
            MainActivity.db.favorite(id, if (favorite == 1) 0 else 1)

            clickAction(btnFavorite, id, if (favorite == 1) 0 else 1)
        }
        btnFavorite.text = if (favorite == 1) "Drop Favorite" else "Add Favorite"
    }

    private fun loadData(id: String) {
        val textViewWidth = findViewById<TextView>(R.id.single_width)
        val textViewHeight = findViewById<TextView>(R.id.single_height)

        api.single(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            rawPhoto = result
                            textViewWidth.text = result.width.toString()
                            textViewHeight.text = result.height.toString()
                        },
                        { error ->
                            Toast.makeText(this@PhotoActivity, error.message + " ", Toast.LENGTH_SHORT).show()
                        }
                )
    }
}
