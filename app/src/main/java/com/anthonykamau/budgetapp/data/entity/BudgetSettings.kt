package com.anthonykamau.budgetapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budget_settings")
data class BudgetSettings(
    @PrimaryKey
    val id: Int = 1,               // always 1 — only one settings row ever exists

    val userName: String = "",     // e.g. "Brian" — used in "Good morning, Brian"

    val monthlyBudget: Double = 0.0, // e.g. 7800.0

    val currency: String = "KES",  // default currency prefix

    val notifyAt80: Boolean = true,  // warn at 80% spend

    val notifyAt100: Boolean = true, // warn at 100% spend

    val dailyReminderOn: Boolean = false, // daily log reminder

    val dailyReminderTime: String = "20:00" // default reminder time
)