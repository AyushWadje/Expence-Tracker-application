package com.example.expencetracker.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.expencetracker.data.local.dao.BudgetDao
import com.example.expencetracker.data.local.dao.CategoryDao
import com.example.expencetracker.data.local.dao.TransactionDao
import com.example.expencetracker.data.local.entity.BudgetEntity
import com.example.expencetracker.data.local.entity.CategoryEntity
import com.example.expencetracker.data.local.entity.TransactionEntity

/**
 * Room database for the Expense Tracker application.
 * Contains three main entities: TransactionEntity, CategoryEntity, and BudgetEntity.
 * Pre-populates the database with default categories on first launch.
 */
@Database(
    entities = [TransactionEntity::class, CategoryEntity::class, BudgetEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ExpenseDatabase : RoomDatabase() {

    abstract fun transactionDao(): TransactionDao
    abstract fun categoryDao(): CategoryDao
    abstract fun budgetDao(): BudgetDao

    companion object {
        @Volatile
        private var INSTANCE: ExpenseDatabase? = null

        /**
         * Get or create a singleton instance of the ExpenseDatabase.
         *
         * @param context Application context
         * @return ExpenseDatabase instance
         */
        fun getInstance(context: Context): ExpenseDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context): ExpenseDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                ExpenseDatabase::class.java,
                "expense_tracker.db"
            )
                .addCallback(DatabaseCallback())
                .build()
        }
    }

    /**
     * Database callback to populate default categories on first launch.
     */
    private class DatabaseCallback : RoomDatabase.Callback() {
        override fun onCreate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
            super.onCreate(db)
            // Pre-populate with default categories
            val defaultCategories = listOf(
                // Expense categories
                "INSERT INTO categories (name, iconName, colorHex, type) VALUES ('Food & Dining', 'restaurant', '#FF9C3E', 'EXPENSE')",
                "INSERT INTO categories (name, iconName, colorHex, type) VALUES ('Transportation', 'directions_car', '#3D9CFF', 'EXPENSE')",
                "INSERT INTO categories (name, iconName, colorHex, type) VALUES ('Entertainment', 'theaters', '#9C3EFF', 'EXPENSE')",
                "INSERT INTO categories (name, iconName, colorHex, type) VALUES ('Shopping', 'shopping_bag', '#FF3E8F', 'EXPENSE')",
                "INSERT INTO categories (name, iconName, colorHex, type) VALUES ('Utilities', 'local_shipping', '#3EFF9C', 'EXPENSE')",
                // Income categories
                "INSERT INTO categories (name, iconName, colorHex, type) VALUES ('Salary', 'savings', '#4CAF50', 'INCOME')",
                "INSERT INTO categories (name, iconName, colorHex, type) VALUES ('Freelance', 'work', '#8BC34A', 'INCOME')",
                "INSERT INTO categories (name, iconName, colorHex, type) VALUES ('Investment', 'trending_up', '#FFC107', 'INCOME')",
                "INSERT INTO categories (name, iconName, colorHex, type) VALUES ('Bonus', 'card_giftcard', '#FF5722', 'INCOME')",
                "INSERT INTO categories (name, iconName, colorHex, type) VALUES ('Other', 'category', '#9E9E9E', 'BOTH')"
            )
            defaultCategories.forEach { query ->
                db.execSQL(query)
            }
        }
    }
}

