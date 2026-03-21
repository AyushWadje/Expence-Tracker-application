package com.example.expencetracker.domain.usecase

import com.example.expencetracker.domain.model.Budget
import com.example.expencetracker.domain.repository.BudgetRepository

/**
 * Use case for setting a budget.
 */
class SetBudgetUseCase(private val repository: BudgetRepository) {
    suspend operator fun invoke(budget: Budget): Long {
        return repository.setBudget(budget)
    }
}

/**
 * Use case for updating a budget.
 */
class UpdateBudgetUseCase(private val repository: BudgetRepository) {
    suspend operator fun invoke(budget: Budget) {
        repository.updateBudget(budget)
    }
}

/**
 * Use case for deleting a budget.
 */
class DeleteBudgetUseCase(private val repository: BudgetRepository) {
    suspend operator fun invoke(budgetId: Long) {
        repository.deleteBudget(budgetId)
    }
}

/**
 * Use case for getting a budget by ID.
 */
class GetBudgetByIdUseCase(private val repository: BudgetRepository) {
    operator fun invoke(budgetId: Long) = repository.getBudgetById(budgetId)
}

/**
 * Use case for getting all budgets.
 */
class GetAllBudgetsUseCase(private val repository: BudgetRepository) {
    operator fun invoke() = repository.getAllBudgets()
}

/**
 * Use case for getting budgets by month and year.
 */
class GetBudgetsByMonthYearUseCase(private val repository: BudgetRepository) {
    operator fun invoke(month: Int, year: Int) =
        repository.getBudgetsByMonthYear(month, year)
}

/**
 * Use case for getting budget for a specific category.
 */
class GetBudgetByCategoryUseCase(private val repository: BudgetRepository) {
    operator fun invoke(categoryId: Long, month: Int, year: Int) =
        repository.getBudgetByCategory(categoryId, month, year)
}

