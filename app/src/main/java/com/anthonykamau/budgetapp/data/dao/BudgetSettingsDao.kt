package com.anthonykamau.budgetapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.anthonykamau.budgetapp.data.entity.BudgetSettings

@Dao
interface BudgetSettingsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSettings(settings: BudgetSettings)

    @Update
    suspend fun updateSettings(settings: BudgetSettings)

    // always returns the single settings row — used everywhere
    @Query("SELECT * FROM budget_settings WHERE id = 1")
    fun getSettings(): LiveData<BudgetSettings>

    // one-time read without LiveData — used on app startup
    @Query("SELECT * FROM budget_settings WHERE id = 1")
    suspend fun getSettingsOnce(): BudgetSettings?
}