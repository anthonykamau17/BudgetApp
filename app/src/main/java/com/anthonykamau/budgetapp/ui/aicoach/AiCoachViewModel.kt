package com.anthonykamau.budgetapp.ui.aicoach

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.anthonykamau.budgetapp.data.db.AppDatabase
import com.anthonykamau.budgetapp.data.entity.ChatMessage
import com.anthonykamau.budgetapp.util.DateUtils
import com.anthonykamau.budgetapp.util.FinancialSensei
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AiCoachViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application)
    private val chatDao = database.chatMessageDao()

    val messages: LiveData<List<ChatMessage>> = chatDao.getAllMessages()
    val isLoading = androidx.lifecycle.MutableLiveData(false)

    fun initIfEmpty() {
        viewModelScope.launch {
            val currentMessages = database.chatMessageDao().getAllMessagesList()
            if (currentMessages.isEmpty()) {
                saveAiMessage(
                    "Hey! I'm your local Money Sensei 🏯\n\n" +
                            "I don't need internet or API keys to help you. Ask me for a \"Status Report\" or just say \"Hello\" to get my latest insights on your spending!"
                )
            }
        }
    }

    fun sendMessage(userMessage: String) {
        viewModelScope.launch {
            // Save user message to DB
            chatDao.insertMessage(ChatMessage(content = userMessage, isUser = true))
            
            isLoading.value = true
            delay(1000)

            try {
                val startOfMonth = DateUtils.getStartOfCurrentMonth()
                val daysLeft = DateUtils.getDaysLeftInMonth()
                val daysElapsed = DateUtils.getDaysElapsedInMonth()

                val settings = database.budgetSettingsDao().getSettingsOnce()
                val expenses = database.expenseDao().getAllExpensesList()
                
                val monthlyBudget = settings?.monthlyBudget ?: 0.0
                val userName = settings?.userName ?: "Student"
                
                val monthExpenses = expenses.filter { it.date >= startOfMonth }
                val totalSpent = monthExpenses.sumOf { it.amount }

                val responseList = FinancialSensei.generateInsights(
                    userName = userName,
                    monthlyBudget = monthlyBudget,
                    totalSpent = totalSpent,
                    daysLeft = daysLeft,
                    daysElapsed = daysElapsed,
                    expenses = monthExpenses
                )
                
                for (insight in responseList) {
                    saveAiMessage(insight)
                    delay(500)
                }

            } catch (e: Exception) {
                saveAiMessage("Sensei is confused: ${e.message}")
            } finally {
                isLoading.postValue(false)
            }
        }
    }

    private suspend fun saveAiMessage(content: String) {
        chatDao.insertMessage(ChatMessage(content = content, isUser = false))
    }

    fun clearChat() {
        viewModelScope.launch {
            chatDao.clearHistory()
            initIfEmpty()
        }
    }
}
