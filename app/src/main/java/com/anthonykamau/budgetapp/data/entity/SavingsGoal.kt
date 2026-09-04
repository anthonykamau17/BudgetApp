package com.anthonykamau.budgetapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "savings_goals")
data class SavingsGoal(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String,              // e.g. "Holiday Fund"

    val emoji: String,             // e.g. "🏖️"

    val targetAmount: Double,      // e.g. 5000.0

    val savedAmount: Double = 0.0, // starts at 0, user updates this

    val targetDate: Long? = null,  // optional deadline, can be null

    val createdAt: Long = System.currentTimeMillis()
)