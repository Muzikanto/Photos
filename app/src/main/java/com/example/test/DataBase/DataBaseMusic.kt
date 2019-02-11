package com.example.test.DataBase

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.test.Music.ClassMusic
import kotlin.collections.ArrayList




class DataBaseMusic(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    fun insertSound(sound: Sound): Boolean {
        val db = writableDatabase

        val values = ContentValues()
        values.put(DBContract.MusicEntry.COL_NAME, sound.name)
        values.put(DBContract.MusicEntry.COL_PATH, sound.path)
        values.put(DBContract.MusicEntry.COL_DURATION, sound.duration)

        db.insert(DBContract.MusicEntry.TABLE_NAME, null, values)

        return true
    }


    fun readSound(id: Int): Sound? {
        var sound: Sound? = null
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select * from " + DBContract.MusicEntry.TABLE_NAME + " WHERE " + DBContract.MusicEntry.COL_ID + "='" + id + "'", null)
        } catch (e: SQLiteException) {
            // if table not yet present, create it
            db.execSQL(SQL_CREATE_ENTRIES)
            return null
        }

        var soundId: String
        var name:String
        var path:String
        var duration: String

        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                soundId = cursor.getString(cursor.getColumnIndex(DBContract.MusicEntry.COL_ID))
                name = cursor.getString(cursor.getColumnIndex(DBContract.MusicEntry.COL_NAME))
                path = cursor.getString(cursor.getColumnIndex(DBContract.MusicEntry.COL_PATH))
                duration = cursor.getString(cursor.getColumnIndex(DBContract.MusicEntry.COL_DURATION))

                sound = Sound(soundId, name, path, duration)
                cursor.moveToNext()
            }
        }
        cursor.close()
        return sound
    }

    fun readAllSound() {
        ClassMusic.vec = ArrayList()
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select * from " + DBContract.MusicEntry.TABLE_NAME, null)
        } catch (e: SQLiteException) {
            db.execSQL(SQL_CREATE_ENTRIES)
        }

        var soundId: String
        var name:String
        var path:String
        var duration: String

        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                soundId = cursor.getString(cursor.getColumnIndex(DBContract.MusicEntry.COL_ID))
                name = cursor.getString(cursor.getColumnIndex(DBContract.MusicEntry.COL_NAME))
                path = cursor.getString(cursor.getColumnIndex(DBContract.MusicEntry.COL_PATH))
                duration = cursor.getString(cursor.getColumnIndex(DBContract.MusicEntry.COL_DURATION))

                ClassMusic.vec.add(Sound(soundId, name, path, duration))
                cursor.moveToNext()
            }
        }
        cursor.close()
    }


    fun deleteSound(id: Int): Boolean {
        val db = writableDatabase
        val selection = DBContract.MusicEntry.COL_ID + " LIKE ?"
        val selectionArgs = arrayOf(id.toString())
        db.delete(DBContract.MusicEntry.TABLE_NAME, selection, selectionArgs)
        return true
    }

    fun dropDataBase() {
        val db = writableDatabase
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }


    companion object {
        val DATABASE_VERSION = 1
        val DATABASE_NAME = "DataBaseMusic.db"

        private val SQL_CREATE_ENTRIES =
                "CREATE TABLE " + DBContract.MusicEntry.TABLE_NAME + " (" +
                        DBContract.MusicEntry.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        DBContract.MusicEntry.COL_NAME + " TEXT," +
                        DBContract.MusicEntry.COL_PATH + " TEXT," +
                        DBContract.MusicEntry.COL_DURATION + " TEXT)"

        private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + DBContract.MusicEntry.TABLE_NAME
    }
}
