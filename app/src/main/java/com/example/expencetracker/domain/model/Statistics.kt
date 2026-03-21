package com.example.expencetracker.domain.model

/**
 * Domain model representing a summary of transactions for a specific month.
 *
 * @property month Month (1-12)
 * @property year Year
 * @property totalIncome Total income for the month
 * @property totalExpense Total expenses for the month
 */
data class MonthlySummary(
    val month: Int,
    val year: Int,
    val totalIncome: Double,
    val totalExpense: Double
) {
    val balance: Double get() = totalIncome - totalExpense
}

/**
 * Domain model representing spending statistics for a category.
 *
 * @property category The category
 * @property amount Total amount spent in this category
 * @property percentage Percentage of total spending
 */
data class CategoryStat(
    val category: Category,
    val amount: Double,
    val percentage: Float
)

/**
 * Domain model representing budget status for a category.
 *
 * @property category The category
 * @property spent Amount spent in this category
 * @property limit Budget limit for this category
 * @property percentage Percentage of budget used
 */
data class BudgetStatus(
    val category: Category,
    val spent: Double,
    val limit: Double
) {
    val percentage: Float get() = if (limit > 0) (spent / limit * 100).toFloat() else 0f
    val remaining: Double get() = maxOf(0.0, limit - spent)
}

/**
 * Domain model representing daily spending statistics.
 *
 * @property day Day of the month
 * @property total Total spent on this day
 */
data class DailyStat(
    val day: Int,
    val total: Double
)

/**
 * Wrapper class for API results with success/error handling.
 */
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    data object Loading : Result<Nothing>()
}

