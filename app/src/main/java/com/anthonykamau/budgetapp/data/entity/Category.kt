package com.anthonykamau.budgetapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String,        // e.g. "Food"

    val emoji: String,       // e.g. "🍔"

    val isDefault: Boolean = true  // true for built-in categories
)