package com.example.test.Music

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import com.example.test.AppPreferences
import com.example.test.R


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
            val remoteViews = RemoteViews(context.packageName, R.layout.widget_music)
            classMusic = ClassMusic(context, null, true)

            when (intent?.action) {
                updateVal -> {
                    Log.d("muzi", "update")
                }
                buttonPlay -> {
                    if (ClassMusic.mediaPlayer.isPlaying) {
                        AppPreferences.lastMoment = ClassMusic.mediaPlayer.currentPosition
                        ClassMusic.mediaPlayer.pause()
                        ClassMusic.timerSeekbar?.cancel()
                    } else {
                        classMusic?.startSound()
                        ClassMusic.mediaPlayer.seekTo(AppPreferences.lastMoment)
                        ClassMusic.mediaPlayer.start()
                    }
                    setIsPlaying(remoteViews)
                }
                buttonPrev -> {
                    AppPreferences.lastMusic--
                    if (AppPreferences.lastMusic < 0)
                        AppPreferences.lastMusic = ClassMusic.vec.size - 1
                    if (ClassMusic.mediaPlayer.isPlaying)
                        classMusic?.startSound()
                    else {
                        classMusic?.startSound()
                        AppPreferences.lastMoment = ClassMusic.mediaPlayer.currentPosition
                        ClassMusic.mediaPlayer.pause()
                        ClassMusic.timerSeekbar?.cancel()
                    }
                }
                buttonNext -> {
                    AppPreferences.lastMusic++
                    if (AppPreferences.lastMusic > ClassMusic.vec.size - 1)
                        AppPreferences.lastMusic = 0
                    if (ClassMusic.mediaPlayer.isPlaying)
                        classMusic?.startSound()
                    else {
                        classMusic?.startSound()
                        AppPreferences.lastMoment = ClassMusic.mediaPlayer.currentPosition
                        ClassMusic.mediaPlayer.pause()
                        ClassMusic.timerSeekbar?.cancel()
                    }
                }
                else -> {
                    return
                }
            }

            remoteViews.setTextViewText(R.id.musicWidgetText, getNameSound())

            val appWidgetManager = AppWidgetManager.getInstance(context)
            appWidgetManager.updateAppWidget(ComponentName(context, javaClass), remoteViews)
        }
    }

    private fun setIsPlaying(remoteViews: RemoteViews) {
        if (ClassMusic.mediaPlayer.isPlaying)
            remoteViews.setTextViewText(R.id.musicWidgetPlay, "Pause")
        else
            remoteViews.setTextViewText(R.id.musicWidgetPlay, "Play")
    }

    private fun getNameSound(): String {
        val str: String
        if (ClassMusic.vec.size > AppPreferences.lastMusic)
            str = (AppPreferences.lastMusic + 1).toString() + ") " + ClassMusic.vec[AppPreferences.lastMusic].name
        else str = "Error"
        return str
    }

    private fun getPendingSelfIntent(context: Context, action: String): PendingIntent {
        val intent = Intent(context, javaClass)
        intent.action = action
        return PendingIntent.getBroadcast(context, 0, intent, 0)
    }
}

