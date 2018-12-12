package com.example.test.Adapter

import android.content.Context
import android.content.Intent
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.test.Alarm.ActivityAddAlarm
import com.example.test.DataBase.AlarmClockItem
import com.example.test.DataBase.DataBaseAlarms
import com.example.test.R
import java.util.ArrayList


class AdapterAlarm(val context: Context, val data: ArrayList<AlarmClockItem>, val listView: ListView?) : BaseAdapter() {
    var mInflater: LayoutInflater
    var dataBaseAlarm: DataBaseAlarms

    init {
        mInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        dataBaseAlarm = DataBaseAlarms(context)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rawView = mInflater.inflate(R.layout.list_alarm, parent, false)
        val timeView = rawView.findViewById<TextView>(R.id.listAlarms_time)
        val weekView = rawView.findViewById<TextView>(R.id.listAlarms_week)
        val switch = rawView.findViewById<Switch>(R.id.listAlarms_enable)
        val buttonDelete = rawView.findViewById<Button>(R.id.listAlarms_delete)
        timeView.setOnClickListener { _ ->
            val intent = Intent(context, ActivityAddAlarm::class.java)
            intent.putExtra("ChangeAlarmId", data[position].id.toString())
            context.startActivity(intent)
        }
        weekView.setOnClickListener { _ ->
            val intent = Intent(context, ActivityAddAlarm::class.java)
            intent.putExtra("ChangeAlarmId", data[position].id.toString())
            context.startActivity(intent)
        }
        timeView.setText(data[position].time)
        weekView.setText(getStrDayFromId(data[position].week))
        switch.isChecked = data[position].isEnable
        switch.setOnClickListener { _ ->
            dataBaseAlarm.updateAlarm(AlarmClockItem(data[position].id, data[position].time, switch.isChecked,
                    data[position].musicPath, data[position].countRepeats, data[position].week, data[position].repeatWeek))
            updateAdapter()
        }
        buttonDelete.setOnClickListener { _ ->
            dataBaseAlarm.deleteAlarm(data[position].id.toString())
            updateAdapter()
        }
        return rawView
    }

    override fun getItem(position: Int): Any {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return data.size
    }

    private fun updateAdapter() {
        val arrAlarm = dataBaseAlarm.readAllAlarms()
        val adapter = AdapterAlarm(context, arrAlarm, listView)
        listView?.adapter = adapter
    }

    private fun getStrDayFromId(week: String): String {
        var str = ""
        for (v in week) {
            if (v != ' ')
                when (v) {
                    '0' -> str += context.resources.getString(R.string.alarmWeekStrSa) + " "
                    '1' -> str += context.resources.getString(R.string.alarmWeekStrSu) + " "
                    '2' -> str += context.resources.getString(R.string.alarmWeekStrMo) + " "
                    '3' -> str += context.resources.getString(R.string.alarmWeekStrTu) + " "
                    '4' -> str += context.resources.getString(R.string.alarmWeekStrWe) + " "
                    '5' -> str += context.resources.getString(R.string.alarmWeekStrTh) + " "
                    '6' -> str += context.resources.getString(R.string.alarmWeekStrFr) + " "
                }
        }
        return str
    }
}