package com.example.test.Database

import android.content.Context
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream
import kotlin.collections.ArrayList

class DBPhotos(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    fun insert(photo: Photo): Boolean {
        val db = writableDatabase

        val values = ContentValues()
        values.put(DBModel.Photo.COL_ID, photo.id)
        val stream = ByteArrayOutputStream()
        photo.bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
        values.put(DBModel.Photo.COL_BLOB, stream.toByteArray())
        values.put(DBModel.Photo.COL_WIDTH, photo.width)
        values.put(DBModel.Photo.COL_HEIGHT, photo.height)

        db.insert(DBModel.Photo.TABLE_NAME, null, values)

        return true
    }

    fun all(): ArrayList<Photo> {
        val sounds = ArrayList<Photo>()
        val db = writableDatabase
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery("select * from " + DBModel.Photo.TABLE_NAME, null)
        } catch (e: SQLiteException) {
            db.execSQL(SQL_CREATE_ENTRIES)
        }

        var width: Int
        var height: Int
        var id: String
        var bitmap: Bitmap

        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                id = cursor.getString(cursor.getColumnIndex(DBModel.Photo.COL_ID))
                width = cursor.getInt(cursor.getColumnIndex(DBModel.Photo.COL_WIDTH))
                height = cursor.getInt(cursor.getColumnIndex(DBModel.Photo.COL_HEIGHT))
                val bytes = cursor.getBlob(cursor.getColumnIndex(DBModel.Photo.COL_BLOB))
                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

                sounds.add(Photo(id, bitmap, width, height))
                cursor.moveToNext()
            }
        }

        cursor.close()

        return sounds
    }

    fun drop() {
        val db = writableDatabase
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }


    companion object {
        val DATABASE_VERSION = 1
        val DATABASE_NAME = "DatebasePhotos.db"

        private val SQL_CREATE_ENTRIES =
                "CREATE TABLE " + DBModel.Photo.TABLE_NAME + " (" +
                        DBModel.Photo.COL_ID + " String," +
                        DBModel.Photo.COL_WIDTH + " Int," +
                        DBModel.Photo.COL_HEIGHT + " Int," +
                        DBModel.Photo.COL_BLOB + " BLOB)"

        private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + DBModel.Photo.TABLE_NAME
    }
}