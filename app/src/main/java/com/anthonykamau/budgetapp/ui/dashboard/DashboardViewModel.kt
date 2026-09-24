package com.anthonykamau.budgetapp.ui.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import com.anthonykamau.budgetapp.data.db.AppDatabase
import com.anthonykamau.budgetapp.data.repository.ExpenseRepository
import com.anthonykamau.budgetapp.data.repository.SavingsGoalRepository
import com.anthonykamau.budgetapp.util.BudgetCalculator
import com.anthonykamau.budgetapp.util.DateUtils
import com.anthonykamau.budgetapp.util.FunnyMessageGenerator
import com.anthonykamau.budgetapp.util.SurvivalCalculator

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application)
    private val expenseRepository = ExpenseRepository(database.expenseDao(), database.categoryDao())
    private val savingsGoalRepository = SavingsGoalRepository(database.savingsGoalDao(), database.budgetSettingsDao())

    private val startOfMonth = DateUtils.getStartOfCurrentMonth()

    val settings = savingsGoalRepository.getSettings()
    val totalSpent = expenseRepository.getTotalSpentFromDate(startOfMonth)
    val recentExpenses = expenseRepository.getRecentExpenses()
    val totalSaved = savingsGoalRepository.getTotalSaved()
    val categoryBudgets = database.categoryBudgetDao().getAllCategoryBudgets()
    val allMonthExpenses = expenseRepository.getExpensesFromDate(startOfMonth)

    val daysLeftInMonth = DateUtils.getDaysLeftInMonth()
    val daysElapsed = DateUtils.getDaysElapsedInMonth()

    val remaining = MediatorLiveData<Double>().apply {
        addSource(totalSpent) { spent ->
            val budget = settings.value?.monthlyBudget ?: 0.0
            value = BudgetCalculator.calculateRemaining(budget, spent ?: 0.0)
        }
        addSource(settings) { s ->
            val budget = s?.monthlyBudget ?: 0.0
            val spent = totalSpent.value ?: 0.0
            value = BudgetCalculator.calculateRemaining(budget, spent)
        }
    }

    val percentageUsed = MediatorLiveData<Double>().apply {
        addSource(totalSpent) { spent ->
            val budget = settings.value?.monthlyBudget ?: 0.0
            value = BudgetCalculator.calculatePercentageUsed(budget, spent ?: 0.0)
        }
        addSource(settings) { s ->
            val budget = s?.monthlyBudget ?: 0.0
            val spent = totalSpent.value ?: 0.0
            value = BudgetCalculator.calculatePercentageUsed(budget, spent)
        }
    }

    // Survival Forecast Data
    val survivalForecast = MediatorLiveData<SurvivalCalculator.Forecast>().apply {
        addSource(totalSpent) { spent ->
            val rem = remaining.value ?: 0.0
            val avg = BudgetCalculator.calculateAverageDailySpend(spent ?: 0.0, daysElapsed)
            value = SurvivalCalculator.calculateForecast(rem, avg)
        }
        addSource(remaining) { rem ->
            val spent = totalSpent.value ?: 0.0
            val avg = BudgetCalculator.calculateAverageDailySpend(spent, daysElapsed)
            value = SurvivalCalculator.calculateForecast(rem ?: 0.0, avg)
        }
    }

    val funnyMessage = MediatorLiveData<String>().apply {
        addSource(percentageUsed) { pct ->
            value = FunnyMessageGenerator.getMessage(BudgetCalculator.getBudgetStatus(pct ?: 0.0))
        }
    }

    val dailyBudgetLeft = MediatorLiveData<Double>().apply {
        addSource(remaining) { rem ->
            value = BudgetCalculator.calculateDailyBudgetLeft(rem ?: 0.0, daysLeftInMonth)
        }
    }
}
