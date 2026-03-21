package com.example.expencetracker.domain.repository

import com.example.expencetracker.domain.model.Budget
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for budget data access.
 */
interface BudgetRepository {

    /**
     * Set a budget for a category.
     *
     * @param budget The budget to set
     * @return The ID of the budget
     */
    suspend fun setBudget(budget: Budget): Long

    /**
     * Update an existing budget.
     *
     * @param budget The budget with updated data
     */
    suspend fun updateBudget(budget: Budget)

    /**
     * Delete a budget.
     *
     * @param budgetId The ID of the budget to delete
     */
    suspend fun deleteBudget(budgetId: Long)

    /**
     * Get a budget by ID.
     *
     * @param budgetId The ID of the budget
     * @return Flow emitting the budget, or null if not found
     */
    fun getBudgetById(budgetId: Long): Flow<Budget?>

    /**
     * Get all budgets.
     *
     * @return Flow emitting all budgets
     */
    fun getAllBudgets(): Flow<List<Budget>>

    /**
     * Get budgets for a specific month and year.
     *
     * @param month Month (1-12)
     * @param year Year
     * @return Flow emitting budgets for the specified period
     */
    fun getBudgetsByMonthYear(month: Int, year: Int): Flow<List<Budget>>

    /**
     * Get budget for a specific category and month.
     *
     * @param categoryId The category ID
     * @param month Month (1-12)
     * @param year Year
     * @return Flow emitting the budget, or null if not found
     */
    fun getBudgetByCategory(categoryId: Long, month: Int, year: Int): Flow<Budget?>
}

