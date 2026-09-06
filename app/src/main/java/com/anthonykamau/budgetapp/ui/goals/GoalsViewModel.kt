package com.anthonykamau.budgetapp.ui.goals

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.anthonykamau.budgetapp.data.db.AppDatabase
import com.anthonykamau.budgetapp.data.entity.SavingsGoal
import com.anthonykamau.budgetapp.data.repository.SavingsGoalRepository
import kotlinx.coroutines.launch

class GoalsViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application)
    private val repository = SavingsGoalRepository(
        database.savingsGoalDao(),
        database.budgetSettingsDao()
    )

    val allGoals = repository.getAllGoals()
    val totalSaved = repository.getTotalSaved()

    fun insertGoal(goal: SavingsGoal) {
        viewModelScope.launch {
            repository.insertGoal(goal)
        }
    }

    fun updateGoal(goal: SavingsGoal) {
        viewModelScope.launch {
            repository.updateGoal(goal)
        }
    }

    fun deleteGoal(goal: SavingsGoal) {
        viewModelScope.launch {
            repository.deleteGoal(goal)
        }
    }
}