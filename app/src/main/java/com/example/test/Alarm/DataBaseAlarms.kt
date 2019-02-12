package com.example.test.Alarm

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.test.DataBase.AlarmClockItem
import com.example.test.DataBase.DBContract


class DataBaseAlarms(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    fun insertAlarm(alarm: AlarmClockItem): Boolean {
        val db = writableDatabase

        val values = ContentValues()

        values.put(DBContract.Alarm.COL_TIME, alarm.time)
        values.put(DBContract.Alarm.COL_IS_ENABLED, alarm.isEnable.toString())
        values.put(DBContract.Alarm.COL_MUSIC_PATH, alarm.musicPath)
        values.put(DBContract.Alarm.COL_REPEATS, alarm.countRepeats)
        values.put(DBContract.Alarm.COL_WEEK, alarm.week)
        values.put(DBContract.Alarm.COL_IS_REPEAT_WEEK, alarm.repeatWeek.toString())
        db.insert(DBContract.Alarm.TABLE_NAME, null, values)

        return true
    }

    fun updateAlarm(alarm: AlarmClockItem): Boolean {
        val db = writableDatabase
        val values = ContentValues()
        values.put(DBContract.Alarm.COL_TIME, alarm.time)
        values.put(DBContract.Alarm.COL_IS_ENABLED, alarm.isEnable.toString())
        values.put(DBContract.Alarm.COL_MUSIC_PATH, alarm.musicPath)
        values.put(DBContract.Alarm.COL_REPEATS, alarm.countRepeats)
        values.put(DBContract.Alarm.COL_WEEK, alarm.week)
        values.put(DBContract.Alarm.COL_IS_REPEAT_WEEK, alarm.repeatWeek.toString())
        db.update(DBContract.Alarm.TABLE_NAME, values, "id=" + alarm.id, null)
        return true
    }

    fun readAlarm(id: Int): ArrayList<AlarmClockItem> {
        val alarms = ArrayList<AlarmClockItem>()
        val db = writableDatabase
        val cursor: Cursor?
        try {
            cursor = db.rawQuery("select * from " + DBContract.Alarm.TABLE_NAME + " WHERE " + DBContract.Alarm.COL_ID + "='" + id + "'", null)
        } catch (e: SQLiteException) {
            // if table not yet present, create it
            db.execSQL(SQL_CREATE_ENTRIES)
            return ArrayList()
        }

        var time: String
        var isEnable: String
        var musicPath: String
        var week: String
        var repeats: Int
        var isRepeatWeek: String

        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                time = cursor.getString(cursor.getColumnIndex(DBContract.Alarm.COL_TIME))
                musicPath = cursor.getString(cursor.getColumnIndex(DBContract.Alarm.COL_MUSIC_PATH))
                isEnable = cursor.getString(cursor.getColumnIndex(DBContract.Alarm.COL_IS_ENABLED))
                week = cursor.getString(cursor.getColumnIndex(DBContract.Alarm.COL_WEEK))
                repeats = cursor.getInt(cursor.getColumnIndex(DBContract.Alarm.COL_REPEATS))
                isRepeatWeek = cursor.getString(cursor.getColumnIndex(DBContract.Alarm.COL_IS_REPEAT_WEEK))

                alarms.add(AlarmClockItem(id, time, isEnable.toBoolean(), musicPath, repeats, week, isRepeatWeek.toBoolean()))

                cursor.moveToNext()
            }
        }
        cursor.close()
        return alarms
    }

    fun readAllAlarms(): ArrayList<AlarmClockItem> {
        val alarms = ArrayList<AlarmClockItem>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select * from " + DBContract.Alarm.TABLE_NAME, null)
        } catch (e: SQLiteException) {
            db.execSQL(SQL_CREATE_ENTRIES)
        }

        var id: Int
        var time: String
        var isEnable: String
        var musicPath: String
        var week: String
        var repeats: Int
        var isRepeatWeek: String

        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                id = cursor.getInt(cursor.getColumnIndex(DBContract.Alarm.COL_ID))
                time = cursor.getString(cursor.getColumnIndex(DBContract.Alarm.COL_TIME))
                musicPath = cursor.getString(cursor.getColumnIndex(DBContract.Alarm.COL_MUSIC_PATH))
                isEnable = cursor.getString(cursor.getColumnIndex(DBContract.Alarm.COL_IS_ENABLED))
                repeats = cursor.getInt(cursor.getColumnIndex(DBContract.Alarm.COL_REPEATS))
                week = cursor.getString(cursor.getColumnIndex(DBContract.Alarm.COL_WEEK))
                isRepeatWeek = cursor.getString(cursor.getColumnIndex(DBContract.Alarm.COL_IS_REPEAT_WEEK))

                alarms.add(AlarmClockItem(id, time, isEnable.toBoolean(), musicPath, repeats, week, isRepeatWeek.toBoolean()))
                cursor.moveToNext()
            }
        }
        cursor.close()
        return alarms
    }


    fun deleteAlarm(id: String): Boolean {
        val db = writableDatabase
        val selection = DBContract.Alarm.COL_ID + " LIKE ?"
        val selectionArgs = arrayOf(id)
        db.delete(DBContract.Alarm.TABLE_NAME, selection, selectionArgs)
        return true
    }

    fun dropDataBase() {
        val db = writableDatabase
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }


    companion object {
        val DATABASE_VERSION = 1
        val DATABASE_NAME = "DataBaseAlarms.db"

        private val SQL_CREATE_ENTRIES =
                "CREATE TABLE " + DBContract.Alarm.TABLE_NAME + " (" +
                        DBContract.Alarm.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        DBContract.Alarm.COL_TIME + " LONG," +
                        DBContract.Alarm.COL_IS_ENABLED + " TEXT," +
                        DBContract.Alarm.COL_MUSIC_PATH + " TEXT," +
                        DBContract.Alarm.COL_REPEATS + " INTEGER," +
                        DBContract.Alarm.COL_IS_REPEAT_WEEK + " TEXT," +
                        DBContract.Alarm.COL_WEEK + " TEXT)"

        private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + DBContract.Alarm.TABLE_NAME
    }
}