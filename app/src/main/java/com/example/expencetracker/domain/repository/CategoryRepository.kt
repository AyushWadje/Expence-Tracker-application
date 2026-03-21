package com.example.expencetracker.domain.repository

import com.example.expencetracker.domain.model.Category
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for category data access.
 */
interface CategoryRepository {

    /**
     * Add a new category.
     *
     * @param category The category to add
     * @return The ID of the newly added category
     */
    suspend fun addCategory(category: Category): Long

    /**
     * Update an existing category.
     *
     * @param category The category with updated data
     */
    suspend fun updateCategory(category: Category)

    /**
     * Delete a category.
     *
     * @param categoryId The ID of the category to delete
     */
    suspend fun deleteCategory(categoryId: Long)

    /**
     * Get a category by ID.
     *
     * @param categoryId The ID of the category
     * @return Flow emitting the category, or null if not found
     */
    fun getCategoryById(categoryId: Long): Flow<Category?>

    /**
     * Get all categories.
     *
     * @return Flow emitting all categories
     */
    fun getAllCategories(): Flow<List<Category>>

    /**
     * Get categories by type.
     *
     * @param type The category type (INCOME, EXPENSE, or BOTH)
     * @return Flow emitting categories of the specified type
     */
    fun getCategoriesByType(type: String): Flow<List<Category>>
}

