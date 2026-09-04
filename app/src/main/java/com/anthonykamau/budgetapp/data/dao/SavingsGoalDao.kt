package com.anthonykamau.budgetapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.anthonykamau.budgetapp.data.entity.SavingsGoal

@Dao
interface SavingsGoalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: SavingsGoal)

    @Update
    suspend fun updateGoal(goal: SavingsGoal)

    @Delete
    suspend fun deleteGoal(goal: SavingsGoal)

    // get all goals ordered by creation date — used in Goals screen
    @Query("SELECT * FROM savings_goals ORDER BY createdAt ASC")
    fun getAllGoals(): LiveData<List<SavingsGoal>>

    // get total saved across all goals — used in Goals screen banner
    @Query("SELECT COALESCE(SUM(savedAmount), 0) FROM savings_goals")
    fun getTotalSaved(): LiveData<Double>
}