package com.example.test.Fragment


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.*
import android.widget.*
import com.example.test.Music.ClassMusic
import com.example.test.Music.ActivityLoadMusic
import com.example.test.R
import android.widget.Toast
import com.example.test.AppPreferences


class MusicFragment : Fragment() {
    lateinit var buttonLoadMusicToBD: Button
    lateinit var butDrop: Button
    lateinit var classMusic: ClassMusic

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_music, container, false)
        buttonLoadMusicToBD = view.findViewById(R.id.FragBl1_LoadMusic)
        butDrop = view.findViewById(R.id.ButtonDropMusicDB)
        setOnclickList()

        if (view != null) {
            classMusic = ClassMusic(context, view, false)

            val buttonPlayMusic = view.findViewById<Button>(R.id.ButtonPlayMusic)
            val buttonPrevMusic = view.findViewById<Button>(R.id.ButtonPrevMusic)
            val buttonNextMusic = view.findViewById<Button>(R.id.ButtonNextMusic)
            classMusic.setWidgets(buttonPlayMusic, buttonPrevMusic, buttonNextMusic)
            classMusic.placeMusic()
            classMusic.loadMusicListView()
        }
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.onCreate(savedInstanceState)
        activity.setTitle(resources.getString(R.string.musicFragmentTitle))
    }

    private fun setOnclickList() {
        buttonLoadMusicToBD.setOnClickListener { _ ->
            val intent = Intent(context, ActivityLoadMusic::class.java)
            startActivityForResult(intent, 100)
        }
        butDrop.setOnClickListener { _ ->
            val builder = AlertDialog.Builder(ContextThemeWrapper(context, R.style.Theme_AppCompat))
            builder.setTitle(resources.getString(R.string.musicDialogDropDB))
                    .setMessage(resources.getString(R.string.musicDialogQustToClear))
                    .setCancelable(false)
                    .setNegativeButton(resources.getString(R.string.textNo), { _, _ ->

                    })
                    .setPositiveButton(resources.getString(R.string.textYes), { _, _ ->
                        classMusic.textView?.setText(resources.getString(R.string.musicFragmentTitle))
                        classMusic.buttonPlayMusic?.setText(resources.getString(R.string.musicButtonPlay))
                        AppPreferences.lastMusic = 0
                        ClassMusic.db.dropDataBase()
                        ClassMusic.vec = ArrayList()
                        classMusic.restoreVecSounds()
                        ClassMusic.mediaPlayer.stop()
                        Toast.makeText(context, resources.getString(R.string.musicDialogToastClear), Toast.LENGTH_SHORT).show()
                    })
                    .create()
                    .show()

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK)
            return
        when (requestCode) {
            100 -> {
                if (data != null) {
                    val arr = data.getStringArrayListExtra("ARRAY_LIST_PATH")
                    if (arr != null && arr.size > 0) {
                        classMusic.AsynkLoadSoundsToBD(arr).execute()
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (AppPreferences.lastMusic < 0)
            AppPreferences.lastMusic = 0
        classMusic.placeMusic()
        ClassMusic.timerSeekbar?.cancel()
        if (ClassMusic.mediaPlayer.isPlaying)
            classMusic.startTimerSeekBar(ClassMusic.sizeSound - AppPreferences.lastMoment.toLong())
    }
}



