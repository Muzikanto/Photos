package com.example.test.Database

import android.provider.BaseColumns

object DBModel {
    class Photo : BaseColumns {
        companion object {
            const val TABLE_NAME = "photos"
            const val COL_ID = "id"
            const val COL_BLOB = "blob"
            const val COL_WIDTH = "width"
            const val COL_HEIGHT = "height"
            const val COL_FAVORITE = "favorite"
        }
    }
}