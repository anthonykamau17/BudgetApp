package com.anthonykamau.budgetapp.util

import kotlin.math.floor

object SurvivalCalculator {

    data class Forecast(
        val daysRemaining: Int,
        val status: SurvivalStatus,
        val message: String
    )

    enum class SurvivalStatus {
        SAFE, WARNING, CRITICAL, BROKE
    }

    fun calculateForecast(
        remainingBudget: Double,
        dailyAverage: Double
    ): Forecast {
        if (remainingBudget <= 0) {
            return Forecast(0, SurvivalStatus.BROKE, "You are officially broke. Ugali and water starts now. 💀")
        }
        
        if (dailyAverage <= 0) {
            return Forecast(30, SurvivalStatus.SAFE, "No spending detected. You're a financial monk! 🧘‍♂️")
        }

        val daysLeft = floor(remainingBudget / dailyAverage).toInt()

        return when {
            daysLeft >= 14 -> Forecast(daysLeft, SurvivalStatus.SAFE, "Comfortable. You've got about $daysLeft days of life left at this rate. 🥂")
            daysLeft >= 7 -> Forecast(daysLeft, SurvivalStatus.WARNING, "Caution. Only $daysLeft days until your wallet goes silent. ⚠️")
            else -> Forecast(daysLeft, SurvivalStatus.CRITICAL, "DANGER! Only $daysLeft days left. Stop breathing near expensive shops! 🚨")
        }
    }
}
