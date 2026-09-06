package com.anthonykamau.budgetapp.ui.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.anthonykamau.budgetapp.data.db.AppDatabase
import com.anthonykamau.budgetapp.data.entity.Expense
import com.anthonykamau.budgetapp.data.repository.ExpenseRepository
import com.anthonykamau.budgetapp.data.repository.SavingsGoalRepository
import com.anthonykamau.budgetapp.util.BudgetCalculator
import com.anthonykamau.budgetapp.util.BudgetStatus
import com.anthonykamau.budgetapp.util.DateUtils
import com.anthonykamau.budgetapp.util.FunnyMessageGenerator

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application)
    private val expenseRepository = ExpenseRepository(
        database.expenseDao(),
        database.categoryDao()
    )
    private val savingsGoalRepository = SavingsGoalRepository(
        database.savingsGoalDao(),
        database.budgetSettingsDao()
    )

    // start of current month timestamp
    private val startOfMonth = DateUtils.getStartOfCurrentMonth()

    // raw data from Room
    val settings = savingsGoalRepository.getSettings()
    val totalSpent = expenseRepository.getTotalSpentFromDate(startOfMonth)
    val recentExpenses = expenseRepository.getRecentExpenses()
    val totalSaved = savingsGoalRepository.getTotalSaved()

    // days left and elapsed in month
    val daysLeftInMonth = DateUtils.getDaysLeftInMonth()
    val daysElapsed = DateUtils.getDaysElapsedInMonth()

    // calculated remaining budget — updates when totalSpent or settings change
    val remaining = MediatorLiveData<Double>().apply {
        addSource(totalSpent) { spent ->
            val budget = settings.value?.monthlyBudget ?: 0.0
            value = BudgetCalculator.calculateRemaining(budget, spent)
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
            value = BudgetCalculator.calculatePercentageUsed(budget, spent)
        }
        addSource(settings) { s ->
            val budget = s?.monthlyBudget ?: 0.0
            val spent = totalSpent.value ?: 0.0
            value = BudgetCalculator.calculatePercentageUsed(budget, spent)
        }
    }

    val dailyBudgetLeft = MediatorLiveData<Double>().apply {
        addSource(remaining) { rem ->
            value = BudgetCalculator.calculateDailyBudgetLeft(rem ?: 0.0, daysLeftInMonth)
        }
    }

    // funny message — updates when spend data changes
    val funnyMessage = MediatorLiveData<String>().apply {
        addSource(percentageUsed) { pct ->
            val status = BudgetCalculator.getBudgetStatus(pct)
            value = FunnyMessageGenerator.getMessage(status)
        }
    }

    // prediction message
    val predictionMessage = MediatorLiveData<String>().apply {
        addSource(totalSpent) { spent ->
            val avgDaily = BudgetCalculator.calculateAverageDailySpend(spent, daysElapsed)
            val rem = remaining.value ?: 0.0
            val daysWillLast = BudgetCalculator.predictDaysMoneyWillLast(rem, avgDaily)
            value = FunnyMessageGenerator.getPredictionMessage(daysLeftInMonth, daysWillLast)
        }
        addSource(remaining) { rem ->
            val spent = totalSpent.value ?: 0.0
            val avgDaily = BudgetCalculator.calculateAverageDailySpend(spent, daysElapsed)
            val daysWillLast = BudgetCalculator.predictDaysMoneyWillLast(rem, avgDaily)
            value = FunnyMessageGenerator.getPredictionMessage(daysLeftInMonth, daysWillLast)
        }
    }

    // budget status for UI color changes
    val budgetStatus = MediatorLiveData<BudgetStatus>().apply {
        addSource(percentageUsed) { pct ->
            value = BudgetCalculator.getBudgetStatus(pct)
        }
    }
}