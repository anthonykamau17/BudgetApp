package com.anthonykamau.budgetapp.ui.addexpense

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.anthonykamau.budgetapp.data.db.AppDatabase
import com.anthonykamau.budgetapp.data.entity.Expense
import com.anthonykamau.budgetapp.data.repository.ExpenseRepository
import kotlinx.coroutines.launch

class AddExpenseViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application)
    private val repository = ExpenseRepository(
        database.expenseDao(),
        database.categoryDao()
    )

    // the amount being typed — starts empty
    val amountInput = MutableLiveData<String>("0")

    // selected category
    val selectedCategoryId = MutableLiveData<Int>(-1)
    val selectedCategoryName = MutableLiveData<String>("")
    val selectedCategoryEmoji = MutableLiveData<String>("")

    // save result — true = success, false = validation failed
    val saveResult = MutableLiveData<SaveResult>()

    // handle keypad button press
    fun onKeyPressed(key: String) {
        val current = amountInput.value ?: "0"
        amountInput.value = when {
            key == "⌫" -> {
                if (current.length <= 1) "0"
                else current.dropLast(1)
            }
            key == "." -> {
                if (current.contains(".")) current // don't add second dot
                else "$current."
            }
            current == "0" -> key // replace leading zero
            else -> "$current$key"
        }
    }

    // select a category
    fun selectCategory(id: Int, name: String, emoji: String) {
        selectedCategoryId.value = id
        selectedCategoryName.value = name
        selectedCategoryEmoji.value = emoji
    }

    // save the expense after validation
    fun saveExpense(note: String) {
        val amountStr = amountInput.value ?: "0"
        val amount = amountStr.toDoubleOrNull() ?: 0.0

        // validation
        if (amount <= 0) {
            saveResult.value = SaveResult.ERROR_AMOUNT
            return
        }
        if (selectedCategoryId.value == -1) {
            saveResult.value = SaveResult.ERROR_CATEGORY
            return
        }

        val expense = Expense(
            amount = amount,
            categoryId = selectedCategoryId.value!!,
            categoryName = selectedCategoryName.value!!,
            categoryEmoji = selectedCategoryEmoji.value!!,
            note = note.ifEmpty { null },
            date = System.currentTimeMillis()
        )

        viewModelScope.launch {
            repository.insertExpense(expense)
            saveResult.postValue(SaveResult.SUCCESS)
        }
    }
}

enum class SaveResult {
    SUCCESS,
    ERROR_AMOUNT,
    ERROR_CATEGORY
}