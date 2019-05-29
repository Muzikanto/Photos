package com.example.test.Database

import android.provider.BaseColumns

object DBModel {
    class Photo : BaseColumns {
        companion object {
            val TABLE_NAME = "classMusic"
            val COL_ID = "id"
            val COL_BLOB = "blob"
            val COL_WIDTH = "width"
            val COL_HEIGHT = "height"
        }
    }
}