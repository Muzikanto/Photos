package com.example.test.Widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.example.test.R
import android.app.PendingIntent
import android.content.ComponentName
import com.example.test.DataBase.DataBaseMusicPlayer
import com.example.test.Music.ClassMusic


class WidgetMusic : AppWidgetProvider() {
    private val buttonPrev = "android.appwidget.action.BUTTON_PREV"
    private val buttonNext = "android.appwidget.action.BUTTON_NEXT"
    private val buttonPlay = "android.appwidget.action.BUTTON_PLAY_PLAYER"
    private val updateVal = "android.appwidget.action.APPWIDGET_UPDATE_OPTIONS"

    var classMusic: ClassMusic? = null


    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            val remoteViews = RemoteViews(context.packageName, R.layout.widget_music)
            remoteViews.setOnClickPendingIntent(R.id.musicWidgetPlay, getPendingSelfIntent(context, buttonPlay))
            remoteViews.setOnClickPendingIntent(R.id.musicWidgetPrev, getPendingSelfIntent(context, buttonPrev))
            remoteViews.setOnClickPendingIntent(R.id.musicWidgetNext, getPendingSelfIntent(context, buttonNext))
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        if (context != null) {
            classMusic = ClassMusic(context, null, true)
            val dbMusicSaves = DataBaseMusicPlayer(context)
            val remoteViews = RemoteViews(context.packageName, R.layout.widget_music)
            val save = dbMusicSaves.read()
            if (save.isPlaying == "true") {

                ClassMusic.lastMusic = save.lastMusic
                ClassMusic.lastMoment = save.lastMoment
                if (ClassMusic.vec.size > 0) {
                    classMusic?.startSound()
                    ClassMusic.timerSeekbar?.cancel()
                    classMusic?.startTimerSeekBar(ClassMusic.sizeSound - ClassMusic.lastMoment.toLong())
                    ClassMusic.mediaPlayer.seekTo(ClassMusic.lastMoment)
                    ClassMusic.mediaPlayer.start()
                    dbMusicSaves.setIsPlaying(false)
                }
            }
            when (intent?.action) {
                updateVal -> {
                }
                buttonPlay -> {
                    ClassMusic.lastMusic = save.lastMusic
                    if (ClassMusic.mediaPlayer.isPlaying) {
                        ClassMusic.lastMoment = ClassMusic.mediaPlayer.currentPosition
                        ClassMusic.mediaPlayer.pause()
                        ClassMusic.timerSeekbar?.cancel()
                    } else {
                        if (!ClassMusic.mediaPlayer.isPlaying)
                            classMusic?.startSound()
                        classMusic?.startTimerSeekBar(ClassMusic.sizeSound - ClassMusic.lastMoment.toLong())
                        ClassMusic.mediaPlayer.seekTo(ClassMusic.lastMoment)
                        ClassMusic.mediaPlayer.start()
                    }
                }
                buttonPrev -> {
                    ClassMusic.lastMusic = save.lastMusic
                    ClassMusic.lastMusic--
                    dbMusicSaves.setMusicLast(ClassMusic.lastMusic)
                    if (ClassMusic.lastMusic < 0)
                        ClassMusic.lastMusic = ClassMusic.vec.size - 1
                    if (ClassMusic.mediaPlayer.isPlaying)
                        classMusic?.startSound()
                    else {
                        classMusic?.startSound()
                        ClassMusic.lastMoment = ClassMusic.mediaPlayer.currentPosition
                        ClassMusic.mediaPlayer.pause()
                        ClassMusic.timerSeekbar?.cancel()
                    }
                }
                buttonNext -> {
                    ClassMusic.lastMusic = save.lastMusic
                    ClassMusic.lastMusic++
                    dbMusicSaves.setMusicLast(ClassMusic.lastMusic)
                    if (ClassMusic.lastMusic > ClassMusic.vec.size - 1)
                        ClassMusic.lastMusic = 0
                    if (ClassMusic.mediaPlayer.isPlaying)
                        classMusic?.startSound()
                    else {
                        classMusic?.startSound()
                        ClassMusic.lastMoment = ClassMusic.mediaPlayer.currentPosition
                        ClassMusic.mediaPlayer.pause()
                        ClassMusic.timerSeekbar?.cancel()
                    }
                }
                else -> {
                    return
                }
            }
            if (ClassMusic.mediaPlayer.isPlaying)
                remoteViews.setTextViewText(R.id.musicWidgetPlay, "Pause")
            else
                remoteViews.setTextViewText(R.id.musicWidgetPlay, "Play")
            val str = getNameSound()
            if (str.isNotEmpty())
                remoteViews.setTextViewText(R.id.musicWidgetText, str)
            val appWidgetManager = AppWidgetManager.getInstance(context)
            appWidgetManager.updateAppWidget(ComponentName(context, javaClass), remoteViews)
        }
    }

    private fun getNameSound(): String {
        var str = ""
        if (ClassMusic.vec.size > ClassMusic.lastMusic) {
            str += (ClassMusic.lastMusic + 1).toString() + ") " + ClassMusic.vec[ClassMusic.lastMusic].name
            if (str.length > 40) {
                val sec = str
                str = ""
                for (i in 0..40)
                    str += sec[i]
                str += "..."
            }
        }
        return str
    }

    private fun getPendingSelfIntent(context: Context, action: String): PendingIntent {
        val intent = Intent(context, javaClass)
        intent.action = action
        return PendingIntent.getBroadcast(context, 0, intent, 0)
    }


}

