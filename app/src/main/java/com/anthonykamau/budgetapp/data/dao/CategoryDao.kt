package com.anthonykamau.budgetapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.anthonykamau.budgetapp.data.entity.Category

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategory(category: Category)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(categories: List<Category>)

    // get all categories — used in Add Expense category grid
    @Query("SELECT * FROM categories ORDER BY id ASC")
    fun getAllCategories(): LiveData<List<Category>>

    // check if categories already seeded — used on first app launch
    @Query("SELECT COUNT(*) FROM categories")
    suspend fun getCategoryCount(): Int
}