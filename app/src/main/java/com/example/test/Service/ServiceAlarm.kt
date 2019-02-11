package com.example.test.Service


import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.test.Alarm.ClassAlarm
import com.example.test.Alarm.DataBaseAlarms


class ServiceAlarm : Service() {
    val alarmClass = ClassAlarm()

    override fun onCreate() {
        val db = DataBaseAlarms(baseContext)
        val arrAlarm = db.readAllAlarms()
        for (value in arrAlarm)
            alarmClass.setAlarm(baseContext, value)
        super.onCreate()
    }

    override fun onBind(intent: Intent?): IBinder {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onDestroy() {
      //  Toast.makeText(baseContext, "Destroy Alarm Service", Toast.LENGTH_SHORT).show()
        alarmClass.clearAlarms(baseContext)
        super.onDestroy()
    }

}