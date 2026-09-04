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

    // get all expenses ordered by date — used in History screen
    @Query("SELECT * FROM expenses ORDER BY date DESC")
    fun getAllExpenses(): LiveData<List<Expense>>

    // get only this month's expenses — used in Dashboard
    @Query("SELECT * FROM expenses WHERE date >= :startOfMonth ORDER BY date DESC")
    fun getExpensesFromDate(startOfMonth: Long): LiveData<List<Expense>>

    // get total spent from a date — used to calculate remaining budget
    @Query("SELECT COALESCE(SUM(amount), 0) FROM expenses WHERE date >= :startOfMonth")
    fun getTotalSpentFromDate(startOfMonth: Long): LiveData<Double>

    // get expenses by category — used in Analytics screen
    @Query("SELECT * FROM expenses WHERE categoryId = :categoryId ORDER BY date DESC")
    fun getExpensesByCategory(categoryId: Int): LiveData<List<Expense>>

    // get total per category — used in Analytics pie/bar breakdown
    @Query("SELECT categoryName, SUM(amount) as total FROM expenses WHERE date >= :startOfMonth GROUP BY categoryName ORDER BY total DESC")
    fun getCategoryTotals(startOfMonth: Long): LiveData<List<CategoryTotal>>

    // get last 5 expenses — used in Dashboard recent list
    @Query("SELECT * FROM expenses ORDER BY createdAt DESC LIMIT 5")
    fun getRecentExpenses(): LiveData<List<Expense>>
}

// helper data class for category totals query — not a Room entity
data class CategoryTotal(
    val categoryName: String,
    val total: Double
)