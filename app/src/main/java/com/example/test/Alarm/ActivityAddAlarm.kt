package com.example.test.Alarm

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import com.example.test.DataBase.AlarmClockItem
import com.example.test.Music.ActivityLoadMusic
import com.example.test.R
import com.example.test.Service.ServiceAlarm
import java.util.*

class ActivityAddAlarm : AppCompatActivity() {
    var musicPath = ""
    var countRepeats = 5
    var week = arrayOf('0', '0', '0', '0', '0', '0', '0')
    var lastHoursStr = ""
    var lastMinStr = ""
    var extraChangeId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_alarm)
        editTextListneer()
        addAlarmButtonListneer()
        setMusicButtonListneer()
        weekButtonsListneer()
        repeatButtonsListneer()
        if (intent.extras != null) {
            val dataBaseAlarms = DataBaseAlarms(applicationContext)
            extraChangeId = intent.getStringExtra("ChangeAlarmId")
            val arr = dataBaseAlarms.readAlarm(extraChangeId.toInt())
            if (arr.isNotEmpty())
                setChangeAlarm(arr[0])
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK)
            return
        when (requestCode) {
            201 -> {
                if (data != null) {
                    val arr = data.getStringArrayListExtra("ARRAY_LIST_PATH")
                    if (arr != null && arr.size > 0) {
                        for (i in 0..arr.size - 1)
                            if (arr[i].contains(".mp3")) {
                                musicPath = arr[i]
                                val viewMusic = findViewById<TextView>(R.id.listAlarmSett_MusicPath)
                                val split = arr[i].split('/')
                                viewMusic.setText(split[split.size - 1])
                                break
                            }
                    }
                }
            }
        }
    }

    private fun addAlarmButtonListneer() {
        val add = findViewById<Button>(R.id.buttonAddAlarm)
        add.setOnClickListener { _ ->
            if (musicPath != "") {
                val intent = Intent()
                val dbAlarm = DataBaseAlarms(applicationContext)
                val time = getTimeStr()
                val weekStr = getWeekStr()
                val switchWeekRepeat = findViewById<Switch>(R.id.listAlarmSett_switchRepeatWeek)
                if (extraChangeId != "") {
                    val dataBaseAlarms = DataBaseAlarms(applicationContext)
                    val arr = dataBaseAlarms.readAlarm(extraChangeId.toInt())
                    if (arr.isNotEmpty())
                        dbAlarm.updateAlarm(AlarmClockItem(arr[0].id, time, true, musicPath, countRepeats, weekStr, switchWeekRepeat.isChecked))
                } else
                    dbAlarm.insertAlarm(AlarmClockItem(0, time, true, musicPath, countRepeats, weekStr, switchWeekRepeat.isChecked))
                stopService(Intent(this, ServiceAlarm::class.java))
                startService(Intent(this, ServiceAlarm::class.java))
                setResult(Activity.RESULT_OK, intent)
                finish()
            } else
                Toast.makeText(applicationContext, resources.getString(R.string.alarmSetMusicToast), Toast.LENGTH_LONG).show()
        }
    }

    private fun editTextListneer() {
        val etH = findViewById<EditText>(R.id.listAlarmSett_timeH)
        val etM = findViewById<EditText>(R.id.listAlarmSett_timeM)
        etH.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val str = etH.text.toString()
                if (str != "") {
                    val value = str.toInt()
                    if (!(value >= 0 && value <= 23))
                        etH.setText(lastHoursStr)
                }
                etH.setSelection(etH.text.toString().length)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                lastHoursStr = etH.text.toString()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
        etM.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val str = etM.text.toString()
                if (str != "") {
                    val value = str.toInt()
                    if (!(value >= 0 && value <= 59))
                        etM.setText(lastMinStr)
                }
                etM.setSelection(etM.text.toString().length)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                lastMinStr = etM.text.toString()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
    }

    private fun setMusicButtonListneer() {
        val setMusic = findViewById<Button>(R.id.listAlarmSett_MusicSet)
        setMusic.setOnClickListener { _ ->
            val intent = Intent(applicationContext, ActivityLoadMusic::class.java)
            startActivityForResult(intent, 201)
        }
    }

    private fun weekButtonsListneer() {
        val week1 = findViewById<Button>(R.id.listAlarmSett_M)
        val week2 = findViewById<Button>(R.id.listAlarmSett_Tu)
        val week3 = findViewById<Button>(R.id.listAlarmSett_W)
        val week4 = findViewById<Button>(R.id.listAlarmSett_Th)
        val week5 = findViewById<Button>(R.id.listAlarmSett_F)
        val week6 = findViewById<Button>(R.id.listAlarmSett_Sa)
        val week7 = findViewById<Button>(R.id.listAlarmSett_Su)
        val buttonsWeek = arrayOf(week1, week2, week3, week4, week5, week6, week7)
        for (i in 0..buttonsWeek.size - 1) {
            buttonsWeek[i].setOnClickListener { _ ->
                if (week[i] == '0') {
                    week[i] = '1'
                    buttonsWeek[i].setBackgroundColor(Color.BLUE)
                } else {
                    buttonsWeek[i].setBackgroundColor(Color.GRAY)
                    week[i] = '0'
                }
            }
            buttonsWeek[i].setBackgroundColor(Color.GRAY)
        }
    }

    private fun repeatButtonsListneer() {
        val rep1 = findViewById<Button>(R.id.listAlarmSett_Repeat1_15)
        val rep2 = findViewById<Button>(R.id.listAlarmSett_Repeat2_10)
        val rep3 = findViewById<Button>(R.id.listAlarmSett_Repeat3_5)
        rep1.setBackgroundColor(Color.BLUE)
        rep2.setBackgroundColor(Color.GRAY)
        rep3.setBackgroundColor(Color.GRAY)
        val buttonsRepeats = arrayOf(rep1, rep2, rep3)
        for (i in 0..buttonsRepeats.size - 1)
            buttonsRepeats[i].setOnClickListener { _ ->
                for (button in buttonsRepeats)
                    button.setBackgroundColor(Color.GRAY)
                buttonsRepeats[i].setBackgroundColor(Color.BLUE)
                countRepeats = (i + 1) * 5
            }
    }

    private fun setChangeAlarm(alarm: AlarmClockItem) {
        val add = findViewById<Button>(R.id.buttonAddAlarm)
        add.setText(R.string.alarmButtonUpdateAlarm)
        val etH = findViewById<EditText>(R.id.listAlarmSett_timeH)
        val etM = findViewById<EditText>(R.id.listAlarmSett_timeM)
        val str = alarm.time.split(':')
        if (str.size > 1) {
            etH.setText(str[0])
            etM.setText(str[1])
        }

        val viewMusic = findViewById<TextView>(R.id.listAlarmSett_MusicPath)
        val split = alarm.musicPath.split('/')
        if (split.size > 1)
            viewMusic.setText(split[split.size - 1])
        musicPath = alarm.musicPath

        val week1 = findViewById<Button>(R.id.listAlarmSett_M)
        val week2 = findViewById<Button>(R.id.listAlarmSett_Tu)
        val week3 = findViewById<Button>(R.id.listAlarmSett_W)
        val week4 = findViewById<Button>(R.id.listAlarmSett_Th)
        val week5 = findViewById<Button>(R.id.listAlarmSett_F)
        val week6 = findViewById<Button>(R.id.listAlarmSett_Sa)
        val week7 = findViewById<Button>(R.id.listAlarmSett_Su)
        if (alarm.week.contains("2")) {
            week[0] = '1'
            week1.setBackgroundColor(Color.BLUE)
        }
        if (alarm.week.contains("3")) {
            week[1] = '1'
            week2.setBackgroundColor(Color.BLUE)
        }
        if (alarm.week.contains("4")) {
            week[2] = '1'
            week3.setBackgroundColor(Color.BLUE)
        }
        if (alarm.week.contains("5")) {
            week[3] = '1'
            week4.setBackgroundColor(Color.BLUE)
        }
        if (alarm.week.contains("6")) {
            week[4] = '1'
            week5.setBackgroundColor(Color.BLUE)
        }
        if (alarm.week.contains("0")) {
            week[5] = '1'
            week6.setBackgroundColor(Color.BLUE)
        }
        if (alarm.week.contains("1")) {
            week[6] = '1'
            week7.setBackgroundColor(Color.BLUE)
        }


        val rep1 = findViewById<Button>(R.id.listAlarmSett_Repeat1_15)
        val rep2 = findViewById<Button>(R.id.listAlarmSett_Repeat2_10)
        val rep3 = findViewById<Button>(R.id.listAlarmSett_Repeat3_5)
        rep1.setBackgroundColor(Color.GRAY)
        when (alarm.countRepeats) {
            1 -> rep1.setBackgroundColor(Color.BLUE)
            2 -> rep2.setBackgroundColor(Color.BLUE)
            3 -> rep3.setBackgroundColor(Color.BLUE)
        }
        countRepeats = alarm.countRepeats

        val switchWeekRepeat = findViewById<Switch>(R.id.listAlarmSett_switchRepeatWeek)
        switchWeekRepeat.isChecked = alarm.repeatWeek
    }

    private fun getTimeStr(): String {
        var time = ""
        val etH = findViewById<EditText>(R.id.listAlarmSett_timeH)
        val etM = findViewById<EditText>(R.id.listAlarmSett_timeM)
        val textH = etH.text.toString()
        val textM = etM.text.toString()
        if (textH == "")
            time += "00"
        else {
            if (textH.length == 1)
                time += "0" + textH
            else
                time += textH
        }
        time += ":"
        if (textM == "")
            time += "00"
        else {
            if (textM.length == 1)
                time += "0" + textM
            else
                time += textM
        }
        return time
    }

    private fun getWeekStr(): String {
        var weekStr = ""
        val weekName = arrayOf("2", "3", "4", "5", "6", "0", "1")
        for (i in 0..week.size - 1)
            if (week[i] == '1')
                weekStr += weekName[i]
        if (weekStr == "") {
            val dat = Date()
            val timeNow = Calendar.getInstance()
            timeNow.time = dat
            weekStr = timeNow.get(Calendar.DAY_OF_WEEK).toString()
        }
        return weekStr
    }
}
