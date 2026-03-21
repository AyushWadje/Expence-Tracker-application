package com.example.expencetracker.di

import com.example.expencetracker.data.local.dao.TransactionDao
import com.example.expencetracker.data.local.dao.CategoryDao
import com.example.expencetracker.data.local.dao.BudgetDao
import com.example.expencetracker.data.repository.TransactionRepositoryImpl
import com.example.expencetracker.data.repository.CategoryRepositoryImpl
import com.example.expencetracker.data.repository.BudgetRepositoryImpl
import com.example.expencetracker.domain.repository.TransactionRepository
import com.example.expencetracker.domain.repository.CategoryRepository
import com.example.expencetracker.domain.repository.BudgetRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for providing repository implementations.
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    /**
     * Provide TransactionRepository implementation.
     */
    @Singleton
    @Provides
    fun provideTransactionRepository(
        transactionDao: TransactionDao,
        categoryDao: CategoryDao
    ): TransactionRepository {
        return TransactionRepositoryImpl(transactionDao, categoryDao)
    }

    /**
     * Provide CategoryRepository implementation.
     */
    @Singleton
    @Provides
    fun provideCategoryRepository(
        categoryDao: CategoryDao
    ): CategoryRepository {
        return CategoryRepositoryImpl(categoryDao)
    }

    /**
     * Provide BudgetRepository implementation.
     */
    @Singleton
    @Provides
    fun provideBudgetRepository(
        budgetDao: BudgetDao
    ): BudgetRepository {
        return BudgetRepositoryImpl(budgetDao)
    }
}

