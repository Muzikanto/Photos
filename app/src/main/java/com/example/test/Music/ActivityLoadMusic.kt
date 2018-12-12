package com.example.test.Music

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import com.example.test.Adapter.AdapterFiles
import com.example.test.DataBase.ListFiles
import com.example.test.R
import java.io.File


class ActivityLoadMusic : AppCompatActivity() {
    lateinit var editText: EditText
    lateinit var listView: ListView

    private var arrList = java.util.ArrayList<ListFiles>()
    private var arrToLoad = ArrayList<String?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load_music)
        editText = findViewById(R.id.editTextPathMusic)
        listView = findViewById(R.id.listViewFileInFolder)
        editText.setText("Music")
        editText.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                checkFolder()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
        val load = findViewById<Button>(R.id.buttonLoadMusic)
        load.setOnClickListener { _ ->
            putArrayToLoad()
        }
        listView.setOnItemClickListener { _, view, position: Int, _ ->
            ClassMusic.lastMusic = position
            var itemPut = true
            for (i in 0..arrToLoad.size-1)
                if (arrToLoad[i] == arrList[position].path) {
                    itemPut = false
                    arrToLoad[i] = null
                }
            if (itemPut) {
                view.setBackgroundColor(Color.BLUE)
                arrToLoad.add(arrList[position].path)
            } else {
                view.setBackgroundColor(Color.BLACK)
            }
        }
        val buttonCheckAll = findViewById<Button>(R.id.buttonLoadFilesCheckAll)
        buttonCheckAll.setOnClickListener { _ ->
            arrToLoad = ArrayList()
            for (v in arrList)
                arrToLoad.add(v.path)
            putArrayToLoad()
        }
        checkFolder()
    }

    private fun pushToArrayContent() {
        arrList = java.util.ArrayList()
        var path = Environment.getExternalStorageDirectory().toString()
        if (editText.text.toString() != "")
            path += "/" + editText.text.toString()
        val file = File(path)

        val arrFiles = ArrayList<ListFiles>()
        val arrDir = ArrayList<ListFiles>()



        if (file.isDirectory) {
            if (file.canRead()) {
                for (temp in file.listFiles()) {
                    if (temp.isDirectory) {
                        val split = temp.absoluteFile.toString().split('/')
                        arrDir.add(ListFiles(split[split.size - 1], true, temp.absoluteFile.toString()))
                    } else if (temp.absoluteFile.toString().contains(".mp3")) {
                        val split = temp.absoluteFile.toString().split('/')
                        arrFiles.add(ListFiles(split[split.size - 1], false, temp.absoluteFile.toString()))
                    }
                }
            } else
                Toast.makeText(applicationContext, "Not found Permission", Toast.LENGTH_LONG).show()
        }

        for (v in arrDir)
            arrList.add(v)
        for (v in arrFiles)
            arrList.add(v)
    }

    private fun putArrayToLoad(){

            val intent = Intent()
            val secondArr = ArrayList<String>()
            for (v in arrToLoad)
                if (v != null)
                    secondArr.add(v)
            intent.putExtra("ARRAY_LIST_PATH", secondArr)
            setResult(Activity.RESULT_OK, intent)
            finish()

    }

    private fun checkFolder(){
        pushToArrayContent()
        val listView = findViewById<ListView>(R.id.listViewFileInFolder)
        val adapter = AdapterFiles(applicationContext, arrList)
        listView.adapter = adapter
    }


}

