package com.anthonykamau.budgetapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val amount: Double,           // e.g. 240.0

    val categoryId: Int,          // links to Category.id

    val categoryName: String,     // e.g. "Food" — stored directly for display

    val categoryEmoji: String,    // e.g. "🍔" — stored for RecyclerView display

    val note: String? = null,     // optional note, can be null

    val date: Long,               // Unix timestamp — the date of the expense

    val createdAt: Long = System.currentTimeMillis() // when it was inserted
)