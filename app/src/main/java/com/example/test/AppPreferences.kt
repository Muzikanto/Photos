package com.example.test

import android.content.Context
import android.content.SharedPreferences

object AppPreferences {
    private val NAME = "MusicPlater"
    private val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences

    private val LAST_MUSIC = Pair("last_moment", 0)
    private val LAST_MOMENT = Pair("last_music", 0)

    fun init(context: Context) {
        preferences = context.getSharedPreferences(NAME, MODE)
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var lastMoment: Int
        get() = preferences.getInt(LAST_MUSIC.first, LAST_MUSIC.second)
        set(value) = preferences.edit {
            it.putInt(LAST_MUSIC.first, value)
        }

    var lastMusic: Int
        get() = preferences.getInt(LAST_MOMENT.first, LAST_MOMENT.second)
        set(value) = preferences.edit {
            it.putInt(LAST_MOMENT.first, value)
        }
}