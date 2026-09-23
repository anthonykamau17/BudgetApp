package com.anthonykamau.budgetapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.anthonykamau.budgetapp.data.entity.Expense

@Dao
interface ExpenseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: Expense)

    @Update
    suspend fun updateExpense(expense: Expense)

    @Delete
    suspend fun deleteExpense(expense: Expense)

    @Query("SELECT * FROM expenses ORDER BY date DESC")
    fun getAllExpenses(): LiveData<List<Expense>>

    // Added non-LiveData direct list fetch for local coach processing
    @Query("SELECT * FROM expenses ORDER BY date DESC")
    suspend fun getAllExpensesList(): List<Expense>

    @Query("SELECT * FROM expenses WHERE date >= :startOfMonth ORDER BY date DESC")
    fun getExpensesFromDate(startOfMonth: Long): LiveData<List<Expense>>

    @Query("SELECT COALESCE(SUM(amount), 0) FROM expenses WHERE date >= :startOfMonth")
    fun getTotalSpentFromDate(startOfMonth: Long): LiveData<Double>

    @Query("SELECT * FROM expenses WHERE categoryId = :categoryId ORDER BY date DESC")
    fun getExpensesByCategory(categoryId: Int): LiveData<List<Expense>>

    @Query("SELECT categoryName, SUM(amount) as total FROM expenses WHERE date >= :startOfMonth GROUP BY categoryName ORDER BY total DESC")
    fun getCategoryTotals(startOfMonth: Long): LiveData<List<CategoryTotal>>

    @Query("SELECT * FROM expenses ORDER BY createdAt DESC LIMIT 5")
    fun getRecentExpenses(): LiveData<List<Expense>>

    @Query("DELETE FROM expenses WHERE date >= :startOfMonth")
    suspend fun deleteExpensesFromDate(startOfMonth: Long)

    @Query("DELETE FROM expenses")
    suspend fun deleteAllExpenses()
}

data class CategoryTotal(
    val categoryName: String,
    val total: Double
)