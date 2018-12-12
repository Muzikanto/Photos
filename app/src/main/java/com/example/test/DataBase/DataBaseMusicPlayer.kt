package com.example.test.DataBase

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.test.Music.ClassMusic




class DataBaseMusicPlayer(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    fun reset(value: ListenSound): Boolean {
        dropDataBase()
        val db = writableDatabase
        val values = ContentValues()
        values.put(DBContract.MusicPlayerSettings.COL_ID, 0)
        values.put(DBContract.MusicPlayerSettings.COL_LAST_MUSIK_ID, value.lastMusic)
        values.put(DBContract.MusicPlayerSettings.COL_LAST_MOMENT, value.lastMoment)
        values.put(DBContract.MusicPlayerSettings.COL_IS_PLAYING, value.isPlaying)

        db.insert(DBContract.MusicPlayerSettings.TABLE_NAME, null, values)
        return true
    }

    fun setIsPlaying(boolean: Boolean) {
        val db = writableDatabase
        val values = ContentValues()
        val value = read()
        values.put(DBContract.MusicPlayerSettings.COL_ID, 0)
        values.put(DBContract.MusicPlayerSettings.COL_LAST_MUSIK_ID, value.lastMusic)
        values.put(DBContract.MusicPlayerSettings.COL_LAST_MOMENT, value.lastMoment)
        values.put(DBContract.MusicPlayerSettings.COL_IS_PLAYING, boolean.toString())
        dropDataBase()
        db.insert(DBContract.MusicPlayerSettings.TABLE_NAME, null, values)
    }

    fun setMomentMusic(moment: Int) {
        val db = writableDatabase
        val values = ContentValues()
        val value = read()
        values.put(DBContract.MusicPlayerSettings.COL_ID, 0)
        values.put(DBContract.MusicPlayerSettings.COL_LAST_MUSIK_ID, value.lastMusic)
        values.put(DBContract.MusicPlayerSettings.COL_LAST_MOMENT, moment.toString())
        values.put(DBContract.MusicPlayerSettings.COL_IS_PLAYING, value.isPlaying)
        dropDataBase()
        db.insert(DBContract.MusicPlayerSettings.TABLE_NAME, null, values)
    }

    fun setMusicLast(music: Int) {
        val db = writableDatabase
        val values = ContentValues()
        val value = read()
        values.put(DBContract.MusicPlayerSettings.COL_ID, 0)
        values.put(DBContract.MusicPlayerSettings.COL_LAST_MUSIK_ID, music.toString())
        values.put(DBContract.MusicPlayerSettings.COL_LAST_MOMENT, value.lastMoment)
        values.put(DBContract.MusicPlayerSettings.COL_IS_PLAYING, value.isPlaying)
        dropDataBase()
        db.insert(DBContract.MusicPlayerSettings.TABLE_NAME, null, values)
    }

    fun read(): ListenSound {
        var sound = ListenSound(ClassMusic.lastMusic, ClassMusic.lastMoment, ClassMusic.mediaPlayer.isPlaying.toString())
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select * from " + DBContract.MusicPlayerSettings.TABLE_NAME + " WHERE "
                    + DBContract.MusicPlayerSettings.COL_ID + "='" + 0 + "'", null)
        } catch (e: SQLiteException) {
            db.execSQL(SQL_CREATE_ENTRIES)
            return sound
        }

        var lastMusic: String
        var lastMoment: String
        var isPlaying: String


        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                lastMusic = cursor.getString(cursor.getColumnIndex(DBContract.MusicPlayerSettings.COL_LAST_MUSIK_ID))
                lastMoment = cursor.getString(cursor.getColumnIndex(DBContract.MusicPlayerSettings.COL_LAST_MOMENT))
                isPlaying = cursor.getString(cursor.getColumnIndex(DBContract.MusicPlayerSettings.COL_IS_PLAYING))
                sound = ListenSound(lastMusic.toInt(), lastMoment.toInt(), isPlaying)
                cursor.moveToNext()
            }
        }
        cursor.close()
        return sound
    }

    private fun dropDataBase() {
        val db = writableDatabase
        db.execSQL(DataBaseMusicPlayer.SQL_DELETE_ENTRIES)
        onCreate(db)
    }


    companion object {
        val DATABASE_VERSION = 1
        val DATABASE_NAME = "DataBaseMusicListen.db"

        private val SQL_CREATE_ENTRIES =
                "CREATE TABLE " + DBContract.MusicPlayerSettings.TABLE_NAME + " (" +
                        DBContract.MusicPlayerSettings.COL_ID + " INTEGER," +
                        DBContract.MusicPlayerSettings.COL_LAST_MUSIK_ID + " TEXT," +
                        DBContract.MusicPlayerSettings.COL_LAST_MOMENT + " TEXT," +
                        DBContract.MusicPlayerSettings.COL_IS_PLAYING + " TEXT)"

        private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + DBContract.MusicPlayerSettings.TABLE_NAME
    }
}
