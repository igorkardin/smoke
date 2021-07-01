package com.simbirsoft.smoke

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.simbirsoft.smoke.ui.BackPressed

class MainActivity : AppCompatActivity() {
    var currentFragment: BackPressed? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onBackPressed() {
        if (currentFragment?.onBackPressed() == false) super.onBackPressed()
    }
}