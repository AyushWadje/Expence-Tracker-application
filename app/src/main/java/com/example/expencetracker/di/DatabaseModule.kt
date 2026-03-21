package com.example.expencetracker.di

import android.content.Context
import androidx.room.Room
import com.example.expencetracker.data.local.database.ExpenseDatabase
import com.example.expencetracker.data.local.dao.TransactionDao
import com.example.expencetracker.data.local.dao.CategoryDao
import com.example.expencetracker.data.local.dao.BudgetDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for providing database and DAO instances.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /**
     * Provide the singleton instance of ExpenseDatabase.
     */
    @Singleton
    @Provides
    fun provideExpenseDatabase(
        @ApplicationContext context: Context
    ): ExpenseDatabase {
        return ExpenseDatabase.getInstance(context)
    }

    /**
     * Provide TransactionDao.
     */
    @Provides
    @Singleton
    fun provideTransactionDao(database: ExpenseDatabase): TransactionDao {
        return database.transactionDao()
    }

    /**
     * Provide CategoryDao.
     */
    @Provides
    @Singleton
    fun provideCategoryDao(database: ExpenseDatabase): CategoryDao {
        return database.categoryDao()
    }

    /**
     * Provide BudgetDao.
     */
    @Provides
    @Singleton
    fun provideBudgetDao(database: ExpenseDatabase): BudgetDao {
        return database.budgetDao()
    }
}

