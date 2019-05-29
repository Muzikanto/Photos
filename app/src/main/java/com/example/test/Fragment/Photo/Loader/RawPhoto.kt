package com.example.test.Fragment.Photo.Loader


class RawPhoto(
        val id: String,
        var urls: Urls,
        val width: Int,
        val height: Int
) {
    class Urls(
            val small: String
    )
}