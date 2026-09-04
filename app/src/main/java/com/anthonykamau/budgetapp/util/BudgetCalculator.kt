package com.anthonykamau.budgetapp.util

object BudgetCalculator {

    // how much is left from the monthly budget
    fun calculateRemaining(monthlyBudget: Double, totalSpent: Double): Double {
        return monthlyBudget - totalSpent
    }

    // what percentage of the budget has been used (0.0 to 100.0)
    fun calculatePercentageUsed(monthlyBudget: Double, totalSpent: Double): Double {
        if (monthlyBudget <= 0) return 0.0
        return (totalSpent / monthlyBudget) * 100
    }

    // how much the user can spend per day for the rest of the month
    fun calculateDailyBudgetLeft(remaining: Double, daysLeftInMonth: Int): Double {
        if (daysLeftInMonth <= 0) return 0.0
        return remaining / daysLeftInMonth
    }

    // predicts how many days the money will last at the current spend rate
    fun predictDaysMoneyWillLast(remaining: Double, averageDailySpend: Double): Int {
        if (averageDailySpend <= 0) return 999 // not spending anything
        return (remaining / averageDailySpend).toInt()
    }

    // calculates average daily spend based on total spent and days elapsed
    fun calculateAverageDailySpend(totalSpent: Double, daysElapsed: Int): Double {
        if (daysElapsed <= 0) return 0.0
        return totalSpent / daysElapsed
    }

    // returns the budget status for UI decisions
    fun getBudgetStatus(percentageUsed: Double): BudgetStatus {
        return when {
            percentageUsed >= 100 -> BudgetStatus.EXCEEDED
            percentageUsed >= 80  -> BudgetStatus.WARNING
            else                  -> BudgetStatus.NORMAL
        }
    }
}

// used by Dashboard to decide what colour and message to show
enum class BudgetStatus {
    NORMAL,   // below 80% — show green/orange normally
    WARNING,  // 80–99%   — show warning message
    EXCEEDED  // 100%+    — show critical message
}