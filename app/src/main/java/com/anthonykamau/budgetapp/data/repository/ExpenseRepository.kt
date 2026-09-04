package com.anthonykamau.budgetapp.data.repository

import androidx.lifecycle.LiveData
import com.anthonykamau.budgetapp.data.dao.CategoryDao
import com.anthonykamau.budgetapp.data.dao.ExpenseDao
import com.anthonykamau.budgetapp.data.dao.CategoryTotal
import com.anthonykamau.budgetapp.data.entity.Category
import com.anthonykamau.budgetapp.data.entity.Expense

class ExpenseRepository(
    private val expenseDao: ExpenseDao,
    private val categoryDao: CategoryDao
) {

    // ── Expense operations ──────────────────────────────

    suspend fun insertExpense(expense: Expense) {
        expenseDao.insertExpense(expense)
    }

    suspend fun updateExpense(expense: Expense) {
        expenseDao.updateExpense(expense)
    }

    suspend fun deleteExpense(expense: Expense) {
        expenseDao.deleteExpense(expense)
    }

    fun getAllExpenses(): LiveData<List<Expense>> {
        return expenseDao.getAllExpenses()
    }

    fun getExpensesFromDate(startOfMonth: Long): LiveData<List<Expense>> {
        return expenseDao.getExpensesFromDate(startOfMonth)
    }

    fun getTotalSpentFromDate(startOfMonth: Long): LiveData<Double> {
        return expenseDao.getTotalSpentFromDate(startOfMonth)
    }

    fun getExpensesByCategory(categoryId: Int): LiveData<List<Expense>> {
        return expenseDao.getExpensesByCategory(categoryId)
    }

    fun getCategoryTotals(startOfMonth: Long): LiveData<List<CategoryTotal>> {
        return expenseDao.getCategoryTotals(startOfMonth)
    }

    fun getRecentExpenses(): LiveData<List<Expense>> {
        return expenseDao.getRecentExpenses()
    }

    // ── Category operations ─────────────────────────────

    fun getAllCategories(): LiveData<List<Category>> {
        return categoryDao.getAllCategories()
    }

    suspend fun getCategoryCount(): Int {
        return categoryDao.getCategoryCount()
    }
}