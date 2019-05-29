package com.example.test

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.support.v4.app.Fragment
import android.os.Bundle
import android.widget.Button
import android.support.v4.app.ActivityCompat
import android.view.*
import com.example.test.Fragment.FavoriteFragment
import com.example.test.Fragment.InfoFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolBar = findViewById<Toolbar>(R.id.toolBar)
        setSupportActionBar(toolBar)

        supportFragmentManager.beginTransaction().add(R.id.fragPlace, PhotoFragment()).commit()
        currentFragment = 1
        findViewById<Button>(R.id.downMenu2).setTextColor(Color.BLUE)

        ActivityCompat.requestPermissions(this, arrayOf(
                android.Manifest.permission.INTERNET
        ), 100)
    }

    fun changeFragment(view: View) {
        val ft = supportFragmentManager.beginTransaction()
        var frag: Fragment = PhotoFragment()
        findViewById<Button>(R.id.downMenu1).setTextColor(Color.WHITE)
        findViewById<Button>(R.id.downMenu2).setTextColor(Color.WHITE)
        findViewById<Button>(R.id.downMenu3).setTextColor(Color.WHITE)
        findViewById<Button>(view.id).setTextColor(Color.BLUE)

        when (view.id) {
            R.id.downMenu1 -> {
                if (currentFragment == 0)
                    return
                frag = FavoriteFragment()
                ft.setCustomAnimations(R.animator.slide_to_left, R.animator.slide_right)
                currentFragment = 0

            }
            R.id.downMenu2 -> {
                if (currentFragment == 1)
                    return
                frag = PhotoFragment()
                if (currentFragment == 0)
                    ft.setCustomAnimations(R.animator.slide_to_right, R.animator.slide_right)
                else
                    ft.setCustomAnimations(R.animator.slide_to_left, R.animator.slide_right)
                currentFragment = 1

            }
            R.id.downMenu3 -> {
                if (currentFragment == 2)
                    return
                frag = InfoFragment()
                ft.setCustomAnimations(R.animator.slide_to_right, R.animator.slide_right)
                currentFragment = 2

            }
        }
        ft.add(R.id.fragPlace, frag).commit()
    }

    companion object {
        var currentFragment = 0
    }
}






