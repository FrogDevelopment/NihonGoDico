package com.frogdevelopment.nihongo.dico.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.frogdevelopment.nihongo.dico.R

class SplashScreenActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen_activity)
        // This method will be executed once the timer is over
        Handler(Looper.myLooper()!!).postDelayed({ launchActivity() }, SPLASH_TIME_OUT)
    }

    private fun launchActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        // close this activity
        finish()
    }

    companion object {
        // Splash screen timer
        private const val SPLASH_TIME_OUT: Long = 500
    }
}