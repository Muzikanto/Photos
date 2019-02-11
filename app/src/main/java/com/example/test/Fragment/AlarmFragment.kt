package com.example.test.Fragment


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.test.R
import android.widget.*
import com.example.test.Alarm.AdapterAlarm
import com.example.test.Alarm.ActivityAddAlarm
import com.example.test.Alarm.ClassAlarm
import com.example.test.Alarm.DataBaseAlarms
import java.util.*


class AlarmFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_alarm, container, false)
        //Button
        val buttonAlarm = view.findViewById<Button>(R.id.buttonAddAlarmSettings)
        buttonAlarm.setOnClickListener { _ ->
            val intent = Intent(context, ActivityAddAlarm::class.java)
            startActivityForResult(intent, 200)
        }
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.onCreate(savedInstanceState)
        activity.setTitle(resources.getString(R.string.alarmFragmentTitle))
        //ListView
        loadToListView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK)
            return
        when (requestCode) {
            200 -> {
                loadToListView()
            }
        }
    }

    private fun loadToListView() {
        val listView = view?.findViewById<ListView>(R.id.listViewAlarms)
        val dbAlarms = DataBaseAlarms(context)
        val arrAlarm = dbAlarms.readAllAlarms()
        val adapter = AdapterAlarm(context, arrAlarm, listView)
        listView?.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        loadToListView()
        val tvNextAlarm = view?.findViewById<TextView>(R.id.textViewNextAlarm)
        var timeNextAlarm = ""
        val arrAlarms = DataBaseAlarms(context).readAllAlarms()
        val dat = Date()
        val timeNow = Calendar.getInstance()
        timeNow.time = dat
        var timeMilissMin: Long = 999999999999999999
        var timeAlarmMin = ""
        for (value in arrAlarms) {
            val arrTime = ClassAlarm.getArrTimeAlarm(value)
            for (vTime in arrTime) {
                if (vTime < timeMilissMin) {
                    timeMilissMin = vTime
                    timeAlarmMin = value.time
                }
            }
        }
        if (timeMilissMin != 999999999999999999) {
            val dayAlarm = Calendar.getInstance()
            dayAlarm.time = Date(timeMilissMin)
            timeNextAlarm = timeAlarmMin + " "
            var day = ""
            if(dayAlarm.get(Calendar.DAY_OF_MONTH) == timeNow.get(Calendar.DAY_OF_MONTH))
                day = resources.getString(R.string.alarmToday)
            else if(dayAlarm.get(Calendar.DAY_OF_MONTH) == timeNow.get(Calendar.DAY_OF_MONTH) + 1)
                day = resources.getString(R.string.alarmNextDay)
            else
                day = dayAlarm.get(Calendar.DAY_OF_MONTH).toString() + "th"
            timeNextAlarm += day
        }
        tvNextAlarm?.setText(timeNextAlarm)
    }
}
