package com.anthonykamau.budgetapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.anthonykamau.budgetapp.data.entity.CategoryBudget

@Dao
interface CategoryBudgetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(categoryBudget: CategoryBudget)

    @Delete
    suspend fun delete(categoryBudget: CategoryBudget)

    @Query("SELECT * FROM category_budgets ORDER BY categoryName ASC")
    fun getAllCategoryBudgets(): LiveData<List<CategoryBudget>>

    @Query("SELECT * FROM category_budgets WHERE categoryName = :categoryName")
    suspend fun getCategoryBudget(categoryName: String): CategoryBudget?

    @Query("DELETE FROM category_budgets")
    suspend fun deleteAll()
}