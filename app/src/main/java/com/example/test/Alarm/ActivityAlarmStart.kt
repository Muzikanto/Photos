package com.example.test.Alarm

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import android.widget.Button
import android.widget.TextView
import android.provider.Settings;
import android.widget.Toast
import com.example.test.DataBase.AlarmClockItem
import com.example.test.DataBase.DataBaseAlarms
import com.example.test.R
import com.example.test.Service.ServiceAlarm

import java.io.File
import java.util.*

class ActivityAlarmStart : AppCompatActivity() {
    lateinit var alarm: AlarmClockItem
    var idAlarm = -1
    var mediaPlayer = MediaPlayer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_start)
        idAlarm = intent.getIntExtra("idAlarm", -1)
        if (idAlarm != -1) {
            val db = DataBaseAlarms(applicationContext)
            val arr = db.readAlarm(idAlarm)
            alarm = arr[0]
            setDataAlarm()
        }
        val stop = findViewById<Button>(R.id.alarmActivity_buttonStop)
        val defer = findViewById<Button>(R.id.alarmActivity_setAsideAlarm)
        stop.setOnClickListener { _ ->
            stopService(Intent(this, ServiceAlarm::class.java))
            startService(Intent(this, ServiceAlarm::class.java))
            mediaPlayer.stop()
            remove()
            finish()
        }
        defer.setOnClickListener { _ ->
            stopService(Intent(this, ServiceAlarm::class.java))
            startService(Intent(this, ServiceAlarm::class.java))
            mediaPlayer.stop()
            setDeferAlarm()
            finish()
        }
    }

    private fun remove() {
        if (idAlarm != -1) {
            val db = DataBaseAlarms(applicationContext)
            if (!alarm.repeatWeek) {
                val weekStr = alarm.week
                val dat = Date()
                val timeNow = Calendar.getInstance()
                timeNow.time = dat
                val dayStr = timeNow.get(Calendar.DAY_OF_WEEK).toString()
                if (weekStr.contains(dayStr)) {
                    val newStr = weekStr.replace(dayStr, "")
                    val alarmClock = AlarmClockItem(alarm.id, alarm.time, alarm.isEnable, alarm.musicPath, alarm.countRepeats, newStr, alarm.repeatWeek)
                    db.updateAlarm(alarmClock)
                }
            }
        }
    }

    private fun setDeferAlarm() {
        if (idAlarm != -1) {
            val db = DataBaseAlarms(applicationContext)
            val split = alarm.time.split(':')
            if (split.size > 1) {
                var newTime = ""
                if (split[1].toInt() < 60 - alarm.countRepeats)
                    newTime = split[0] + ":" + (split[1].toInt() + alarm.countRepeats).toString()
                else {
                    if (split[0].toInt() + 1 < 24)
                        newTime = (split[0].toInt() + 1).toString() + split[1]
                }
                val alarmClock = AlarmClockItem(alarm.id, newTime, alarm.isEnable, alarm.musicPath, alarm.countRepeats, alarm.week, alarm.repeatWeek)
                db.updateAlarm(alarmClock)
            }
        }
    }

    private fun setDataAlarm() {
        val uri = Uri.parse(alarm.musicPath)
        if (File(alarm.musicPath).isFile)
            mediaPlayer = MediaPlayer.create(applicationContext, uri)
        else
            mediaPlayer = MediaPlayer.create(applicationContext, Settings.System.DEFAULT_RINGTONE_URI)
        mediaPlayer.start()
        val time = findViewById<TextView>(R.id.alarmActivity_time)
        time.setText(alarm.time)
        val deferTextview = findViewById<TextView>(R.id.alarmActivity_deferTextView)
        deferTextview.setText(resources.getString(R.string.alarmStartNextAlarmAfter) + alarm.countRepeats.toString())
    }

}
