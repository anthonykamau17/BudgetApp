package com.anthonykamau.budgetapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.anthonykamau.budgetapp.data.dao.BudgetSettingsDao
import com.anthonykamau.budgetapp.data.dao.CategoryDao
import com.anthonykamau.budgetapp.data.dao.ExpenseDao
import com.anthonykamau.budgetapp.data.dao.SavingsGoalDao
import com.anthonykamau.budgetapp.data.entity.BudgetSettings
import com.anthonykamau.budgetapp.data.entity.Category
import com.anthonykamau.budgetapp.data.entity.Expense
import com.anthonykamau.budgetapp.data.entity.SavingsGoal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        Expense::class,
        Category::class,
        SavingsGoal::class,
        BudgetSettings::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    // Room generates the implementation of these automatically
    abstract fun expenseDao(): ExpenseDao
    abstract fun categoryDao(): CategoryDao
    abstract fun savingsGoalDao(): SavingsGoalDao
    abstract fun budgetSettingsDao(): BudgetSettingsDao

    companion object {
        // Singleton — only one instance of the database ever exists
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "budget_database"
                )
                    .addCallback(DatabaseCallback()) // seeds categories on first launch
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    // seeds the categories table the very first time the app runs
    private class DatabaseCallback : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    seedCategories(database.categoryDao())
                    seedDefaultSettings(database.budgetSettingsDao())
                }
            }
        }

        // inserts the 7 default categories on first install
        suspend fun seedCategories(categoryDao: CategoryDao) {
            val defaultCategories = listOf(
                Category(name = "Food", emoji = "🍔"),
                Category(name = "Transport", emoji = "🚌"),
                Category(name = "Books", emoji = "📚"),
                Category(name = "Coffee", emoji = "☕"),
                Category(name = "Rent", emoji = "🏠"),
                Category(name = "Health", emoji = "💊"),
                Category(name = "Fun", emoji = "🎮")
            )
            categoryDao.insertAll(defaultCategories)
        }

        // inserts one default settings row on first install
        suspend fun seedDefaultSettings(budgetSettingsDao: BudgetSettingsDao) {
            val defaultSettings = BudgetSettings(
                id = 1,
                userName = "",
                monthlyBudget = 0.0,
                currency = "KES"
            )
            budgetSettingsDao.insertSettings(defaultSettings)
        }
    }
}