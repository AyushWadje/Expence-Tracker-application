package com.example.expencetracker.domain.model

/**
 * Domain model representing a budget limit for a category.
 *
 * @property id Unique identifier for the budget
 * @property categoryId ID of the category this budget applies to
 * @property limitAmount Maximum budget amount allowed
 * @property month Month (1-12) for which the budget applies
 * @property year Year for which the budget applies
 */
data class Budget(
    val id: Long,
    val categoryId: Long,
    val limitAmount: Double,
    val month: Int,
    val year: Int
)

