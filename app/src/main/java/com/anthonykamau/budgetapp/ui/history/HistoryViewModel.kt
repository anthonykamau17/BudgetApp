package com.anthonykamau.budgetapp.ui.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.anthonykamau.budgetapp.data.db.AppDatabase
import com.anthonykamau.budgetapp.data.entity.Expense
import com.anthonykamau.budgetapp.data.repository.ExpenseRepository
import kotlinx.coroutines.launch

class HistoryViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application)
    private val repository = ExpenseRepository(
        database.expenseDao(),
        database.categoryDao()
    )

    val allExpenses = repository.getAllExpenses()

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch {
            repository.deleteExpense(expense)
        }
    }
}