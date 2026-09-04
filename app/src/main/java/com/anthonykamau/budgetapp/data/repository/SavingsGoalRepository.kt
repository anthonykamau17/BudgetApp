package com.anthonykamau.budgetapp.data.repository

import androidx.lifecycle.LiveData
import com.anthonykamau.budgetapp.data.dao.BudgetSettingsDao
import com.anthonykamau.budgetapp.data.dao.SavingsGoalDao
import com.anthonykamau.budgetapp.data.entity.BudgetSettings
import com.anthonykamau.budgetapp.data.entity.SavingsGoal

class SavingsGoalRepository(
    private val savingsGoalDao: SavingsGoalDao,
    private val budgetSettingsDao: BudgetSettingsDao
) {

    // ── Savings Goal operations ─────────────────────────

    suspend fun insertGoal(goal: SavingsGoal) {
        savingsGoalDao.insertGoal(goal)
    }

    suspend fun updateGoal(goal: SavingsGoal) {
        savingsGoalDao.updateGoal(goal)
    }

    suspend fun deleteGoal(goal: SavingsGoal) {
        savingsGoalDao.deleteGoal(goal)
    }

    fun getAllGoals(): LiveData<List<SavingsGoal>> {
        return savingsGoalDao.getAllGoals()
    }

    fun getTotalSaved(): LiveData<Double> {
        return savingsGoalDao.getTotalSaved()
    }

    // ── Budget Settings operations ──────────────────────

    suspend fun insertSettings(settings: BudgetSettings) {
        budgetSettingsDao.insertSettings(settings)
    }

    suspend fun updateSettings(settings: BudgetSettings) {
        budgetSettingsDao.updateSettings(settings)
    }

    fun getSettings(): LiveData<BudgetSettings> {
        return budgetSettingsDao.getSettings()
    }

    suspend fun getSettingsOnce(): BudgetSettings? {
        return budgetSettingsDao.getSettingsOnce()
    }
}