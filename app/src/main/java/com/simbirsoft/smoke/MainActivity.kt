package com.simbirsoft.smoke

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.simbirsoft.smoke.data.BackPressed
import com.simbirsoft.smoke.ui.BaseFragment

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