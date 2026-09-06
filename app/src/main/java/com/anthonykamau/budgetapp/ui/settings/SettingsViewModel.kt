package com.anthonykamau.budgetapp.ui.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.anthonykamau.budgetapp.data.db.AppDatabase
import com.anthonykamau.budgetapp.data.entity.BudgetSettings
import com.anthonykamau.budgetapp.data.repository.SavingsGoalRepository
import kotlinx.coroutines.launch
import com.anthonykamau.budgetapp.data.repository.ExpenseRepository

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application)
    private val repository = SavingsGoalRepository(
        database.savingsGoalDao(),
        database.budgetSettingsDao()
    )

    val settings = repository.getSettings()
    val saveResult = MutableLiveData<Boolean>()

    fun saveSettings(
        userName: String,
        monthlyBudget: Double,
        currency: String,
        notifyAt80: Boolean,
        notifyAt100: Boolean
    ) {
        viewModelScope.launch {
            val current = repository.getSettingsOnce()
            val updated = BudgetSettings(
                id = 1,
                userName = userName.trim(),
                monthlyBudget = monthlyBudget,
                currency = currency.trim().ifEmpty { "KES" },
                notifyAt80 = notifyAt80,
                notifyAt100 = notifyAt100,
                dailyReminderOn = current?.dailyReminderOn ?: false,
                dailyReminderTime = current?.dailyReminderTime ?: "20:00"
            )
            repository.updateSettings(updated)
            saveResult.postValue(true)
        }
    }

    fun resetMonthExpenses() {
        viewModelScope.launch {
            val repository = ExpenseRepository(
                database.expenseDao(),
                database.categoryDao()
            )
            val startOfMonth = com.anthonykamau.budgetapp.util.DateUtils.getStartOfCurrentMonth()
            repository.deleteExpensesFromDate(startOfMonth)
        }
    }

    fun clearAllData() {
        viewModelScope.launch {
            val repository = ExpenseRepository(
                database.expenseDao(),
                database.categoryDao()
            )
            repository.deleteAllExpenses()
        }
    }
}