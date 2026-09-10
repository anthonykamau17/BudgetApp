package com.anthonykamau.budgetapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // single prefs instance for everything
        val prefs = getSharedPreferences("budget_prefs", MODE_PRIVATE)

        // restore saved theme
        when (prefs.getString("theme", "system")) {
            "dark" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            "light" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }

        setContentView(R.layout.activity_splash)

        // check if onboarding has been seen
        val onboardingSeen = prefs.getBoolean("onboarding_seen", false)

        Handler(Looper.getMainLooper()).postDelayed({
            if (onboardingSeen) {
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                startActivity(Intent(this, OnboardingActivity::class.java))
            }
            finish()
        }, 1800)
    }
}