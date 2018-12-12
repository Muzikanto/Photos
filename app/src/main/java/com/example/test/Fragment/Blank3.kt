package com.example.test.Fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.test.R

class Blank3 : Fragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.frag3, container, false)
        setOnclickListneer(view)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.onCreate(savedInstanceState)
        activity.setTitle("LoadImage")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK && data != null)
            return
        when (requestCode) {
            101 -> {
                val uri = data?.data
                val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                val imageView = view?.findViewById<ImageView>(R.id.FragBl3_ImageView)
                var scale = 1

                if (imageView != null) {
                    val scaleW = bitmap.width / imageView.width
                    val scaleH = bitmap.height / imageView.height
                    if (bitmap.width > imageView.width && scaleW > scaleH)
                        scale = scaleW
                    else if (bitmap.height > imageView.height && scaleW <= scaleH)
                        scale = scaleH
                }

                val bitmapScaled = Bitmap.createScaledBitmap(bitmap, bitmap.width / scale, bitmap.height / scale, false)

                imageView?.setImageBitmap(bitmapScaled)
                Toast.makeText(context, "Access", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setOnclickListneer(view: View) {
        val loadButtonImage = view.findViewById<Button>(R.id.FragBl3_LoadImages)
        loadButtonImage.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val intent = Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                intent.type = "image/*"
                startActivityForResult(intent, 101)
            }
        })
    }

}



