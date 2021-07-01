package com.simbirsoft.smoke

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.launchActivity
import com.simbirsoft.smoke.databinding.ActivitySplashBinding
import kotlin.concurrent.fixedRateTimer

class SplashActivity : AppCompatActivity(R.layout.activity_splash) {
    private val binding by viewBinding<ActivitySplashBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Glide.with(binding.root)
            .load(R.drawable.giphy)
            .into(binding.gif)
        lifecycleScope.launchWhenStarted {
            fixedRateTimer(initialDelay = 3000L, period = 10000L) {
                launchActivity<MainActivity>()
                this.cancel()
                finish()
            }
        }
    }
}