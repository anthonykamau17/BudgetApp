package com.anthonykamau.budgetapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.anthonykamau.budgetapp.data.dao.BudgetSettingsDao
import com.anthonykamau.budgetapp.data.dao.CategoryBudgetDao
import com.anthonykamau.budgetapp.data.dao.CategoryDao
import com.anthonykamau.budgetapp.data.dao.ExpenseDao
import com.anthonykamau.budgetapp.data.dao.SavingsGoalDao
import com.anthonykamau.budgetapp.data.dao.ChatMessageDao
import com.anthonykamau.budgetapp.data.entity.BudgetSettings
import com.anthonykamau.budgetapp.data.entity.Category
import com.anthonykamau.budgetapp.data.entity.CategoryBudget
import com.anthonykamau.budgetapp.data.entity.Expense
import com.anthonykamau.budgetapp.data.entity.SavingsGoal
import com.anthonykamau.budgetapp.data.entity.ChatMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        Expense::class,
        Category::class,
        SavingsGoal::class,
        BudgetSettings::class,
        CategoryBudget::class,
        ChatMessage::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun expenseDao(): ExpenseDao
    abstract fun categoryDao(): CategoryDao
    abstract fun savingsGoalDao(): SavingsGoalDao
    abstract fun budgetSettingsDao(): BudgetSettingsDao
    abstract fun categoryBudgetDao(): CategoryBudgetDao
    abstract fun chatMessageDao(): ChatMessageDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS `category_budgets` " +
                            "(`categoryName` TEXT NOT NULL, " +
                            "`budgetLimit` REAL NOT NULL, " +
                            "`emoji` TEXT NOT NULL, " +
                            "PRIMARY KEY(`categoryName`))"
                )
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS `chat_messages` " +
                            "(`id` INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL, " +
                            "`content` TEXT NOT NULL, " +
                            "`isUser` INTEGER NOT NULL, " +
                            "`timestamp` INTEGER NOT NULL)"
                )
            }
        }

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "budget_database"
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .fallbackToDestructiveMigration()
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

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
