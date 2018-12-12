package com.example.test.Alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.widget.Toast
import android.os.PowerManager
import android.content.BroadcastReceiver
import android.content.Context
import com.example.test.DataBase.AlarmClockItem
import com.example.test.DataBase.DataBaseAlarms
import com.example.test.R
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList


class ClassAlarm : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val pm = context?.getSystemService(Context.POWER_SERVICE) as PowerManager
        val wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "")
        wl.acquire()
        Toast.makeText(context, context.resources.getString(R.string.alarmToastText), Toast.LENGTH_LONG).show() // For example
        if (intent != null) {
            val idAlarm = intent.getIntExtra("idAlarm", -1)
            val newIntent = Intent(context, ActivityAlarmStart::class.java)
            newIntent.putExtra("idAlarm", idAlarm)
            context.startActivity(newIntent)
        }
        resetAlarms(context)
        wl.release()
    }

    fun setAlarm(context: Context, alarm: AlarmClockItem) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ClassAlarm::class.java)
        intent.putExtra("idAlarm", alarm.id)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)

        val time = getArrTimeAlarm(alarm)

        for (value in time)
            alarmManager.set(AlarmManager.RTC_WAKEUP, value, pendingIntent)
    }

    fun clearAlarms(context: Context) {
        val intent = Intent(context, ClassAlarm::class.java)
        val sender = PendingIntent.getBroadcast(context, 0, intent, 0)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(sender)
    }

    fun resetAlarms(context: Context) {
        clearAlarms(context)
        val db = DataBaseAlarms(context)
        val arrAlarm = db.readAllAlarms()
        for (value in arrAlarm)
            setAlarm(context, value)
    }

    companion object {
        fun getArrTimeAlarm(alarm: AlarmClockItem): ArrayList<Long> {
            if(alarm.isEnable) {
                val dat = Date()//initializes to now
                val timeAlarm = Calendar.getInstance()
                val timeNow = Calendar.getInstance()
                timeNow.time = dat
                timeAlarm.time = dat
                val timeStr = alarm.time.split(':')
                if (timeStr.size > 1) {
                    timeAlarm.set(Calendar.HOUR_OF_DAY, timeStr[0].toInt())
                    timeAlarm.set(Calendar.MINUTE, timeStr[1].toInt())
                }
                timeAlarm.set(Calendar.SECOND, 0)

                val time = ArrayList<Long>()

                if (alarm.week.contains("2")) {
                    timeAlarm.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
                    time.add(timeAlarm.timeInMillis)
                }
                if (alarm.week.contains("3")) {
                    timeAlarm.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY)
                    time.add(timeAlarm.timeInMillis)
                }
                if (alarm.week.contains("4")) {
                    timeAlarm.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY)
                    time.add(timeAlarm.timeInMillis)
                }
                if (alarm.week.contains("5")) {
                    timeAlarm.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY)
                    time.add(timeAlarm.timeInMillis)
                }
                if (alarm.week.contains("6")) {
                    timeAlarm.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY)
                    time.add(timeAlarm.timeInMillis)
                }
                if (alarm.week.contains("0")) {
                    timeAlarm.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY)
                    time.add(timeAlarm.timeInMillis)
                }
                if (alarm.week.contains("1")) {
                    timeAlarm.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
                    time.add(timeAlarm.timeInMillis)
                }

                for (i in 0..time.size - 1) {
                    val calendar = Calendar.getInstance()
                    calendar.timeInMillis = time[i]
                    if (calendar.before(timeNow))
                        time[i] = timeAlarmBeforeNowTime(calendar, timeNow)
                }
                return time
            } else
                return ArrayList()
        }

        private fun timeAlarmBeforeNowTime(timeAlarm: Calendar, timeNow: Calendar): Long {
            val countDays = timeNow.getActualMaximum(Calendar.DAY_OF_MONTH)
            var day = timeNow.get(Calendar.DAY_OF_MONTH)
            if (day + 7 - TimeUnit.DAYS.convert(timeNow.timeInMillis - timeAlarm.timeInMillis, TimeUnit.MILLISECONDS).toInt() > countDays) {
                timeAlarm.set(Calendar.DAY_OF_MONTH, countDays - day)
                val month = timeNow.get(Calendar.MONTH)
                if (month + 1 > 12)
                    timeAlarm.set(Calendar.MONTH, month + 1)
            } else {
                day += 7 - TimeUnit.DAYS.convert(timeNow.timeInMillis - timeAlarm.timeInMillis, TimeUnit.MILLISECONDS).toInt()
                timeAlarm.set(Calendar.DAY_OF_MONTH, day)
            }
            return timeAlarm.timeInMillis
        }
    }
}