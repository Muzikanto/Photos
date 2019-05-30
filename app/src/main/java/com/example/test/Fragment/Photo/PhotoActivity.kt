package com.example.test

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.test.Database.Photo
import com.example.test.Fragment.Photo.Loader.PhotoApi
import com.example.test.Fragment.Photo.Loader.RawPhoto
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
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
        val textViewWidth = findViewById<TextView>(R.id.single_width)
        val textViewHeight = findViewById<TextView>(R.id.single_height)
        val btnFavorite = findViewById<Button>(R.id.single_btn_favorite)

        val photoId = intent.getStringExtra("id")
        photo = MainActivity.db.find(photoId)
        imageView.setImageBitmap(photo.bitmap)


        if (photo.favorite == 1) {
            btnFavorite.setOnClickListener {
                MainActivity.db.favorite(photoId, 0)
                btnFavorite.text = "Add Favorite"
            }
            btnFavorite.text = "Drop Favorite"
        } else {
            btnFavorite.setOnClickListener {
                MainActivity.db.favorite(photoId, 1)
                btnFavorite.text = "Drop Favorite"
            }
            btnFavorite.text = "Add Favorite"
        }


        api.single(photoId)
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
