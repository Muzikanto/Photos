package com.example.test.DataBase

import android.provider.BaseColumns

object DBContract {
    class MusicEntry : BaseColumns {
        companion object {
            val TABLE_NAME = "classMusic"
            val COL_PATH = "path"
            val COL_NAME = "name"
            val COL_DURATION = "duration"
            val COL_ID = "id"
        }
    }

    class Alarm :BaseColumns{
        companion object {
            val TABLE_NAME = "alarms"
            val COL_IS_ENABLED = "enabled"
            val COL_TIME = "time"
            val COL_MUSIC_PATH = "classMusic"
            val COL_WEEK = "week"
            val COL_REPEATS = "repeats"
            val COL_IS_REPEAT_WEEK = "repeatWeek"
            val COL_ID = "id"
        }
    }
}