package com.anthonykamau.budgetapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category_budgets")
data class CategoryBudget(
    @PrimaryKey
    val categoryName: String, // e.g. "Food" — matches expense categoryName
    val budgetLimit: Double,  // e.g. 3000.0
    val emoji: String         // e.g. "🍔"
)