package com.example.expencetracker.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.expencetracker.data.local.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for CategoryEntity.
 * Provides methods to query and modify category data in the Room database.
 */
@Dao
interface CategoryDao {

    /**
     * Insert a new category into the database.
     *
     * @param category The category entity to insert
     * @return The row ID of the inserted category
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: CategoryEntity): Long

    /**
     * Insert multiple categories.
     *
     * @param categories List of category entities to insert
     * @return List of row IDs of inserted categories
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(categories: List<CategoryEntity>): List<Long>

    /**
     * Update an existing category.
     *
     * @param category The category entity with updated data
     */
    @Update
    suspend fun update(category: CategoryEntity)

    /**
     * Delete a category.
     *
     * @param category The category entity to delete
     */
    @Delete
    suspend fun delete(category: CategoryEntity)

    /**
     * Get a category by its ID.
     *
     * @param id The category ID
     * @return Flow emitting the category, or null if not found
     */
    @Query("SELECT * FROM categories WHERE id = :id")
    fun getCategoryById(id: Long): Flow<CategoryEntity?>

    /**
     * Get all categories.
     *
     * @return Flow emitting the list of all categories
     */
    @Query("SELECT * FROM categories ORDER BY name ASC")
    fun getAllCategories(): Flow<List<CategoryEntity>>

    /**
     * Get categories filtered by type.
     *
     * @param type The category type (INCOME, EXPENSE, or BOTH)
     * @return Flow emitting categories of the specified type
     */
    @Query("SELECT * FROM categories WHERE type = :type OR type = 'BOTH' ORDER BY name ASC")
    fun getCategoriesByType(type: String): Flow<List<CategoryEntity>>

    /**
     * Delete all categories (used for testing or data reset).
     */
    @Query("DELETE FROM categories")
    suspend fun deleteAll()
}

