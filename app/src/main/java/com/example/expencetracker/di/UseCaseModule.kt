package com.example.expencetracker.di

import com.example.expencetracker.domain.repository.TransactionRepository
import com.example.expencetracker.domain.repository.CategoryRepository
import com.example.expencetracker.domain.repository.BudgetRepository
import com.example.expencetracker.domain.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for providing use case instances.
 */
@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    // Transaction Use Cases
    @Singleton
    @Provides
    fun provideAddTransactionUseCase(repository: TransactionRepository) =
        AddTransactionUseCase(repository)

    @Singleton
    @Provides
    fun provideUpdateTransactionUseCase(repository: TransactionRepository) =
        UpdateTransactionUseCase(repository)

    @Singleton
    @Provides
    fun provideDeleteTransactionUseCase(repository: TransactionRepository) =
        DeleteTransactionUseCase(repository)

    @Singleton
    @Provides
    fun provideGetTransactionByIdUseCase(repository: TransactionRepository) =
        GetTransactionByIdUseCase(repository)

    @Singleton
    @Provides
    fun provideGetAllTransactionsUseCase(repository: TransactionRepository) =
        GetAllTransactionsUseCase(repository)

    @Singleton
    @Provides
    fun provideGetPagedTransactionsUseCase(repository: TransactionRepository) =
        GetPagedTransactionsUseCase(repository)

    @Singleton
    @Provides
    fun provideGetFilteredTransactionsUseCase(repository: TransactionRepository) =
        GetFilteredTransactionsUseCase(repository)

    @Singleton
    @Provides
    fun provideSearchTransactionsUseCase(repository: TransactionRepository) =
        SearchTransactionsUseCase(repository)

    @Singleton
    @Provides
    fun provideGetTransactionsByCategoryUseCase(repository: TransactionRepository) =
        GetTransactionsByCategoryUseCase(repository)

    @Singleton
    @Provides
    fun provideGetMonthlySummaryUseCase(repository: TransactionRepository) =
        GetMonthlySummaryUseCase(repository)

    // Category Use Cases
    @Singleton
    @Provides
    fun provideAddCategoryUseCase(repository: CategoryRepository) =
        AddCategoryUseCase(repository)

    @Singleton
    @Provides
    fun provideUpdateCategoryUseCase(repository: CategoryRepository) =
        UpdateCategoryUseCase(repository)

    @Singleton
    @Provides
    fun provideDeleteCategoryUseCase(repository: CategoryRepository) =
        DeleteCategoryUseCase(repository)

    @Singleton
    @Provides
    fun provideGetCategoryByIdUseCase(repository: CategoryRepository) =
        GetCategoryByIdUseCase(repository)

    @Singleton
    @Provides
    fun provideGetAllCategoriesUseCase(repository: CategoryRepository) =
        GetAllCategoriesUseCase(repository)

    @Singleton
    @Provides
    fun provideGetCategoriesByTypeUseCase(repository: CategoryRepository) =
        GetCategoriesByTypeUseCase(repository)

    // Budget Use Cases
    @Singleton
    @Provides
    fun provideSetBudgetUseCase(repository: BudgetRepository) =
        SetBudgetUseCase(repository)

    @Singleton
    @Provides
    fun provideUpdateBudgetUseCase(repository: BudgetRepository) =
        UpdateBudgetUseCase(repository)

    @Singleton
    @Provides
    fun provideDeleteBudgetUseCase(repository: BudgetRepository) =
        DeleteBudgetUseCase(repository)

    @Singleton
    @Provides
    fun provideGetBudgetByIdUseCase(repository: BudgetRepository) =
        GetBudgetByIdUseCase(repository)

    @Singleton
    @Provides
    fun provideGetAllBudgetsUseCase(repository: BudgetRepository) =
        GetAllBudgetsUseCase(repository)

    @Singleton
    @Provides
    fun provideGetBudgetsByMonthYearUseCase(repository: BudgetRepository) =
        GetBudgetsByMonthYearUseCase(repository)

    @Singleton
    @Provides
    fun provideGetBudgetByCategoryUseCase(repository: BudgetRepository) =
        GetBudgetByCategoryUseCase(repository)
}

