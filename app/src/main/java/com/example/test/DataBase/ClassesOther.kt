package com.example.test.DataBase

class ListFiles(val name: String, val isDirectory: Boolean, val path: String)

class AlarmClockItem(val id: Int, val time: String, val isEnable: Boolean, val musicPath: String, val countRepeats: Int, val week: String, val repeatWeek: Boolean)

class Sound(val id: Int?, val name: String, val path: String, val duration: String, val directory: String)
