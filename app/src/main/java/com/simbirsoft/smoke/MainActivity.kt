package com.simbirsoft.smoke

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.simbirsoft.smoke.ui.BaseFragment

class MainActivity : AppCompatActivity() {
    var currentFragment: BaseFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onBackPressed() {
        if (currentFragment?.onBackPressed() == false) super.onBackPressed()
    }
}