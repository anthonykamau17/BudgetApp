package com.anthonykamau.budgetapp.ui.analytics

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.anthonykamau.budgetapp.data.dao.CategoryTotal
import com.anthonykamau.budgetapp.data.db.AppDatabase
import com.anthonykamau.budgetapp.data.entity.Expense
import com.anthonykamau.budgetapp.data.repository.ExpenseRepository
import com.anthonykamau.budgetapp.util.DateUtils
import java.util.Calendar

class AnalyticsViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application)
    private val repository = ExpenseRepository(
        database.expenseDao(),
        database.categoryDao()
    )

    // week expenses
    val weekExpenses: LiveData<List<Expense>> =
        repository.getExpensesFromDate(getStartOfWeek())

    // month expenses
    val monthExpenses: LiveData<List<Expense>> =
        repository.getExpensesFromDate(DateUtils.getStartOfCurrentMonth())

    // semester expenses — Aug 1 of current year
    val semesterExpenses: LiveData<List<Expense>> =
        repository.getExpensesFromDate(getStartOfSemester())

    // category totals for month
    val categoryTotals: LiveData<List<CategoryTotal>> =
        repository.getCategoryTotals(DateUtils.getStartOfCurrentMonth())

    private fun getStartOfWeek(): Long {
        val calendar = Calendar.getInstance()
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        val daysFromMonday = (dayOfWeek - Calendar.MONDAY + 7) % 7
        calendar.add(Calendar.DAY_OF_YEAR, -daysFromMonday)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
    private fun getStartOfSemester(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MONTH, Calendar.AUGUST)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
}