package com.example.expencetracker.domain.model

/**
 * Domain model representing an expense/income category.
 *
 * @property id Unique identifier for the category
 * @property name Category name
 * @property iconName Icon identifier for UI rendering
 * @property colorHex Hex color code for visual representation
 * @property type Category type: INCOME, EXPENSE, or BOTH
 */
data class Category(
    val id: Long,
    val name: String,
    val iconName: String,
    val colorHex: String,
    val type: CategoryType
)

/**
 * Enumeration for category types.
 */
enum class CategoryType {
    INCOME, EXPENSE, BOTH
}

