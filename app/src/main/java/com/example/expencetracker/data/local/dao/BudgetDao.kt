package com.example.expencetracker.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.expencetracker.data.local.entity.BudgetEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for BudgetEntity.
 * Provides methods to query and modify budget data in the Room database.
 */
@Dao
interface BudgetDao {

    /**
     * Insert a new budget into the database.
     *
     * @param budget The budget entity to insert
     * @return The row ID of the inserted budget
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(budget: BudgetEntity): Long

    /**
     * Insert multiple budgets.
     *
     * @param budgets List of budget entities to insert
     * @return List of row IDs of inserted budgets
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(budgets: List<BudgetEntity>): List<Long>

    /**
     * Update an existing budget.
     *
     * @param budget The budget entity with updated data
     */
    @Update
    suspend fun update(budget: BudgetEntity)

    /**
     * Delete a budget.
     *
     * @param budget The budget entity to delete
     */
    @Delete
    suspend fun delete(budget: BudgetEntity)

    /**
     * Get a budget by its ID.
     *
     * @param id The budget ID
     * @return Flow emitting the budget, or null if not found
     */
    @Query("SELECT * FROM budgets WHERE id = :id")
    fun getBudgetById(id: Long): Flow<BudgetEntity?>

    /**
     * Get all budgets.
     *
     * @return Flow emitting the list of all budgets
     */
    @Query("SELECT * FROM budgets ORDER BY month DESC, year DESC")
    fun getAllBudgets(): Flow<List<BudgetEntity>>

    /**
     * Get budgets for a specific month and year.
     *
     * @param month Month (1-12)
     * @param year Year
     * @return Flow emitting budgets for the specified month/year
     */
    @Query("SELECT * FROM budgets WHERE month = :month AND year = :year ORDER BY categoryId ASC")
    fun getBudgetsByMonthYear(month: Int, year: Int): Flow<List<BudgetEntity>>

    /**
     * Get budget for a specific category and month.
     *
     * @param categoryId The category ID
     * @param month Month (1-12)
     * @param year Year
     * @return Flow emitting the budget, or null if not found
     */
    @Query(
        "SELECT * FROM budgets WHERE categoryId = :categoryId AND month = :month AND year = :year"
    )
    fun getBudgetByCategory(categoryId: Long, month: Int, year: Int): Flow<BudgetEntity?>

    /**
     * Delete all budgets (used for testing or data reset).
     */
    @Query("DELETE FROM budgets")
    suspend fun deleteAll()
}

