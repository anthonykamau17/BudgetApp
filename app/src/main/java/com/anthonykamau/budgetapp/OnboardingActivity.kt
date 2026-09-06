package com.anthonykamau.budgetapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.viewpager2.widget.ViewPager2

class OnboardingActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var llDots: LinearLayout
    private lateinit var btnNext: CardView
    private lateinit var tvBtnLabel: TextView
    private lateinit var tvSkip: TextView

    private val slides = listOf(
        OnboardingSlide(
            emoji = "💰",
            title = "Welcome to BudgetApp",
            description = "The money tracking app built for Kenyan university students. Know where your money goes before it disappears.",
            tag = "Made for students"
        ),
        OnboardingSlide(
            emoji = "⚙️",
            title = "Set your budget",
            description = "Tell us how much you have for the month. We'll track every shilling and warn you before you hit zero.",
            tag = "Takes 30 seconds"
        ),
        OnboardingSlide(
            emoji = "⚡",
            title = "Log in under 3 seconds",
            description = "Quick add buttons for Food, Fare, Books and Coffee. Tap, enter amount, done. No excuses not to track.",
            tag = "Super fast"
        ),
        OnboardingSlide(
            emoji = "🎯",
            title = "Hit your goals",
            description = "Saving for a laptop, holiday or new phone? Set a goal and watch the progress bar grow. You've got this.",
            tag = "Stay motivated"
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        viewPager = findViewById(R.id.viewpager_onboarding)
        llDots = findViewById(R.id.ll_dots)
        btnNext = findViewById(R.id.btn_next)
        tvBtnLabel = findViewById(R.id.tv_btn_label)
        tvSkip = findViewById(R.id.tv_skip)

        // set up adapter
        viewPager.adapter = OnboardingAdapter(slides)

        // build dots
        setupDots(0)

        // page change listener
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                setupDots(position)
                if (position == slides.size - 1) {
                    tvBtnLabel.text = "Get started"
                    tvSkip.visibility = View.GONE
                } else {
                    tvBtnLabel.text = "Next"
                    tvSkip.visibility = View.VISIBLE
                }
            }
        })

        // next button
        btnNext.setOnClickListener {
            if (viewPager.currentItem < slides.size - 1) {
                viewPager.currentItem++
            } else {
                finishOnboarding()
            }
        }

        // skip button
        tvSkip.setOnClickListener {
            finishOnboarding()
        }
    }

    private fun setupDots(activeIndex: Int) {
        llDots.removeAllViews()
        val params = LinearLayout.LayoutParams(16, 16).apply {
            setMargins(6, 0, 6, 0)
        }

        slides.indices.forEach { i ->
            val dot = View(this).apply {
                layoutParams = params
                background = if (i == activeIndex) {
                    resources.getDrawable(R.drawable.dot_active, null)
                } else {
                    resources.getDrawable(R.drawable.dot_inactive, null)
                }
            }
            llDots.addView(dot)
        }
    }

    private fun finishOnboarding() {
        // mark onboarding as seen
        getSharedPreferences("budget_prefs", MODE_PRIVATE)
            .edit()
            .putBoolean("onboarding_seen", true)
            .apply()

        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}