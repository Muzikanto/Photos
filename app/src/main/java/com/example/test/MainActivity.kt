package com.example.test



import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.widget.Button
import android.widget.Toast
import android.app.Notification
import android.app.NotificationManager
import android.support.v4.app.ActivityCompat
import android.view.*
import com.example.test.DataBase.DataBaseAlarms
import com.example.test.DataBase.DataBaseMusic
import com.example.test.Music.ClassMusic
import com.example.test.Fragment.StartFragment
import com.example.test.DataBase.DataBaseMusicPlayer
import com.example.test.DataBase.ListenSound
import com.example.test.Fragment.MusicFragment
import com.example.test.Fragment.AlarmFragment
import com.example.test.Fragment.Blank3

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //ToolBar
        val toolBar = findViewById<Toolbar>(R.id.toolBar)
        setSupportActionBar(toolBar)
        //Fragments
        supportFragmentManager.beginTransaction().add(R.id.fragPlace, AlarmFragment()).commit()
        findViewById<Button>(R.id.toBlank1).setTextColor(Color.BLUE)
        fragIndexSecond = 0
        //Permissions
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 100)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //  val item = menu?.findItem(R.id.menu_SignIn)
        //  val str = MainActivity.user.nickName
        //  item?.title = str
        menuInflater.inflate(R.menu.drop_down, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_SignIn -> {

            }
            R.id.menuDrop_1 -> {
                MainActivity.showDialog(this, "Title", "Text", "Neutral")
            }
            R.id.menuDrop_2 -> {
                MainActivity.showNotification(applicationContext, "test", "Notification", "First Message", 100)
            }
            R.id.menuDrop_3 -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 100)
        when (requestCode) {
            100 -> {

            }
        }
    }

    fun changeFragment(view: View) {
        val ft = supportFragmentManager.beginTransaction()
        var frag: Fragment = StartFragment()
        findViewById<Button>(R.id.toBlank1).setTextColor(Color.WHITE)
        findViewById<Button>(R.id.toBlank2).setTextColor(Color.WHITE)
        findViewById<Button>(R.id.toBlank3).setTextColor(Color.WHITE)
        findViewById<Button>(view.id).setTextColor(Color.BLUE)
        when (view.id) {
            R.id.toBlank1 -> {
                if (fragIndexSecond == 0)
                    return
                frag = MusicFragment()
                ft.setCustomAnimations(R.animator.slide_to_left, R.animator.slide_right)
                fragIndexSecond = 0

            }
            R.id.toBlank2 -> {
                if (fragIndexSecond == 1)
                    return
                frag = AlarmFragment()
                if (fragIndexSecond == 0)
                    ft.setCustomAnimations(R.animator.slide_to_right, R.animator.slide_right)
                else
                    ft.setCustomAnimations(R.animator.slide_to_left, R.animator.slide_right)
                fragIndexSecond = 1

            }
            R.id.toBlank3 -> {
                if (fragIndexSecond == 2)
                    return
                frag = Blank3()
                ft.setCustomAnimations(R.animator.slide_to_right, R.animator.slide_right)
                fragIndexSecond = 2

            }
        }
        ft.add(R.id.fragPlace, frag).commit()
    }

    companion object {
        var fragIndexSecond = 0

        fun showDialog(context: Context, title: String, text: String, neutral: String) {
            val builder = AlertDialog.Builder(ContextThemeWrapper(context, R.style.Theme_AppCompat))
            builder.setTitle(title)
                    .setMessage(text)
                    .setCancelable(false)
                    .setNegativeButton("Yes", { _, _ ->
                        Toast.makeText(context, "Yes", Toast.LENGTH_SHORT).show()
                    })
                    .setPositiveButton("No", { _, _ ->
                        Toast.makeText(context, "No", Toast.LENGTH_SHORT).show()
                    })
                    .setNeutralButton(neutral, { _, _ ->
                        Toast.makeText(context, "Neutral", Toast.LENGTH_SHORT).show()
                    })
                    .create()
                    .show()
        }

        fun showNotification(context: Context, ticker: String, title: String, text: String, id: Int) {
            val intent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)
            val notificationBuilder = Notification.Builder(context, "com.example.test")
            notificationBuilder.setContentIntent(pendingIntent)
                    .setContentText(text)
                    .setTicker(ticker)
                    .setContentTitle(title)
                    .setAutoCancel(true)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.abc_ic_star_black_48dp)
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notification = notificationBuilder.build()
            notification.defaults = Notification.DEFAULT_ALL
            notificationManager.notify(id, notification)
        }

    }

    override fun onStop() {
        super.onStop()
        val dbMusicSaves = DataBaseMusicPlayer(applicationContext)
        dbMusicSaves.reset(ListenSound(ClassMusic.lastMusic, ClassMusic.lastMoment, ClassMusic.mediaPlayer.isPlaying.toString()))
    }
}






