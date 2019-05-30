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

    fun favorite(id: String, favorite: Int): Boolean {
        val db = writableDatabase
        try {
            db.execSQL("update ${DBModel.Photo.TABLE_NAME} set ${DBModel.Photo.COL_FAVORITE}=$favorite where id=\"$id\"")
        } catch (e: SQLiteException) {
            return false
        }

        return true
    }

    fun find(id: String): Photo {
        val db = writableDatabase

        val cursor = db.rawQuery("select * from ${DBModel.Photo.TABLE_NAME} where ${DBModel.Photo.COL_ID}=\"$id\"", null)
        cursor.moveToFirst()

        val width = cursor.getInt(cursor.getColumnIndex(DBModel.Photo.COL_WIDTH))
        val height = cursor.getInt(cursor.getColumnIndex(DBModel.Photo.COL_HEIGHT))
        val bytes = cursor.getBlob(cursor.getColumnIndex(DBModel.Photo.COL_BLOB))
        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        val favorite = cursor.getInt(cursor.getColumnIndex(DBModel.Photo.COL_FAVORITE))

        cursor.close()

        return Photo(id, bitmap, width, height, favorite)
    }

    fun all(need_favorite: Int): ArrayList<Photo> {
        val photos = ArrayList<Photo>()
        val db = writableDatabase
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery("select * from ${DBModel.Photo.TABLE_NAME} where ${DBModel.Photo.COL_FAVORITE}=$need_favorite", null)
        } catch (e: SQLiteException) {
            db.execSQL(SQL_CREATE_ENTRIES)
        }

        var width: Int
        var height: Int
        var id: String
        var bitmap: Bitmap
        var favorite: Int

        if (cursor!!.moveToFirst()) {
            while (!cursor.isAfterLast) {
                id = cursor.getString(cursor.getColumnIndex(DBModel.Photo.COL_ID))
                width = cursor.getInt(cursor.getColumnIndex(DBModel.Photo.COL_WIDTH))
                height = cursor.getInt(cursor.getColumnIndex(DBModel.Photo.COL_HEIGHT))
                val bytes = cursor.getBlob(cursor.getColumnIndex(DBModel.Photo.COL_BLOB))
                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                favorite = cursor.getInt(cursor.getColumnIndex(DBModel.Photo.COL_FAVORITE))

                photos.add(Photo(id, bitmap, width, height, favorite))
                cursor.moveToNext()
            }
        }

        cursor.close()

        return photos
    }

    fun dropPhotos(): Boolean {
        val db = writableDatabase

        try {
            db.execSQL("DELETE FROM " + DBModel.Photo.TABLE_NAME + " WHERE ${DBModel.Photo.COL_FAVORITE}=0")
        } catch (e: SQLiteException) {
            return false
        }

        return true
    }

    fun dropTable(): Boolean {
        val db = writableDatabase

        db.execSQL(SQL_DELETE_ENTRIES)

        return true
    }


    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "DatabasePhotos.db"

        private val SQL_CREATE_ENTRIES =
                "CREATE TABLE IF NOT EXISTS " + DBModel.Photo.TABLE_NAME + " (" +
                        DBModel.Photo.COL_ID + " String not null unique," +
                        DBModel.Photo.COL_WIDTH + " Int," +
                        DBModel.Photo.COL_HEIGHT + " Int," +
                        DBModel.Photo.COL_FAVORITE + " Int default 0," +
                        DBModel.Photo.COL_BLOB + " BLOB)"


        private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + DBModel.Photo.TABLE_NAME
    }
}