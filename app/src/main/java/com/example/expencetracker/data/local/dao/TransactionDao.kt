package com.example.expencetracker.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.expencetracker.data.local.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for TransactionEntity.
 * Provides methods to query and modify transaction data in the Room database.
 */
@Dao
interface TransactionDao {

    /**
     * Insert a new transaction into the database.
     *
     * @param transaction The transaction entity to insert
     * @return The row ID of the inserted transaction
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaction: TransactionEntity): Long

    /**
     * Insert multiple transactions.
     *
     * @param transactions List of transaction entities to insert
     * @return List of row IDs of inserted transactions
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(transactions: List<TransactionEntity>): List<Long>

    /**
     * Update an existing transaction.
     *
     * @param transaction The transaction entity with updated data
     */
    @Update
    suspend fun update(transaction: TransactionEntity)

    /**
     * Delete a transaction.
     *
     * @param transaction The transaction entity to delete
     */
    @Delete
    suspend fun delete(transaction: TransactionEntity)

    /**
     * Get a transaction by its ID.
     *
     * @param id The transaction ID
     * @return Flow emitting the transaction, or null if not found
     */
    @Query("SELECT * FROM transactions WHERE id = :id")
    fun getTransactionById(id: Long): Flow<TransactionEntity?>

    /**
     * Get all transactions ordered by date descending.
     *
     * @return Flow emitting the list of all transactions
     */
    @Query("SELECT * FROM transactions ORDER BY dateTime DESC")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    /**
     * Get paginated transactions ordered by date descending.
     *
     * @return PagingSource for paginated transaction queries
     */
    @Query("SELECT * FROM transactions ORDER BY dateTime DESC")
    fun getPaginatedTransactions(): PagingSource<Int, TransactionEntity>

    /**
     * Get transactions filtered by type and date range.
     *
     * @param type Transaction type (INCOME or EXPENSE)
     * @param startDate Start date in epoch milliseconds
     * @param endDate End date in epoch milliseconds
     * @return Flow emitting filtered transactions
     */
    @Query(
        """
        SELECT * FROM transactions 
        WHERE type = :type AND dateTime >= :startDate AND dateTime <= :endDate
        ORDER BY dateTime DESC
        """
    )
    fun getTransactionsByTypeAndDateRange(
        type: String,
        startDate: Long,
        endDate: Long
    ): Flow<List<TransactionEntity>>

    /**
     * Get transactions filtered by category.
     *
     * @param categoryId The category ID to filter by
     * @return Flow emitting transactions of the specified category
     */
    @Query("SELECT * FROM transactions WHERE categoryId = :categoryId ORDER BY dateTime DESC")
    fun getTransactionsByCategory(categoryId: Long): Flow<List<TransactionEntity>>

    /**
     * Search transactions by note field.
     *
     * @param query The search query string
     * @return Flow emitting transactions matching the search query
     */
    @Query(
        """
        SELECT * FROM transactions 
        WHERE note LIKE '%' || :query || '%'
        ORDER BY dateTime DESC
        """
    )
    fun searchTransactions(query: String): Flow<List<TransactionEntity>>

    /**
     * Get transactions for a specific month and year.
     *
     * @param month Month (1-12)
     * @param year Year
     * @return Flow emitting transactions for the specified month/year
     */
    @Query(
        """
        SELECT * FROM transactions 
        WHERE strftime('%m', dateTime / 1000, 'unixepoch') = :month 
        AND strftime('%Y', dateTime / 1000, 'unixepoch') = :year
        ORDER BY dateTime DESC
        """
    )
    fun getTransactionsByMonthYear(month: String, year: String): Flow<List<TransactionEntity>>

    /**
     * Get all expenses for a specific month and category.
     *
     * @param categoryId The category ID
     * @param month Month (1-12)
     * @param year Year
     * @return Flow emitting transactions matching the criteria
     */
    @Query(
        """
        SELECT * FROM transactions 
        WHERE categoryId = :categoryId 
        AND type = 'EXPENSE'
        AND strftime('%m', dateTime / 1000, 'unixepoch') = :month 
        AND strftime('%Y', dateTime / 1000, 'unixepoch') = :year
        ORDER BY dateTime DESC
        """
    )
    fun getExpensesByMonthYearCategory(
        categoryId: Long,
        month: String,
        year: String
    ): Flow<List<TransactionEntity>>

    /**
     * Get the total sum of expenses for a category in a specific month.
     *
     * @param categoryId The category ID
     * @param month Month (1-12)
     * @param year Year
     * @return Flow emitting the sum of expenses, or 0.0 if none
     */
    @Query(
        """
        SELECT COALESCE(SUM(amount), 0.0) FROM transactions 
        WHERE categoryId = :categoryId 
        AND type = 'EXPENSE'
        AND strftime('%m', dateTime / 1000, 'unixepoch') = :month 
        AND strftime('%Y', dateTime / 1000, 'unixepoch') = :year
        """
    )
    fun getMonthlyExpenseByCategory(
        categoryId: Long,
        month: String,
        year: String
    ): Flow<Double>

    /**
     * Get total income for a specific month.
     *
     * @param month Month (1-12)
     * @param year Year
     * @return Flow emitting the total income for the month
     */
    @Query(
        """
        SELECT COALESCE(SUM(amount), 0.0) FROM transactions 
        WHERE type = 'INCOME'
        AND strftime('%m', dateTime / 1000, 'unixepoch') = :month 
        AND strftime('%Y', dateTime / 1000, 'unixepoch') = :year
        """
    )
    fun getMonthlyIncome(month: String, year: String): Flow<Double>

    /**
     * Get total expenses for a specific month.
     *
     * @param month Month (1-12)
     * @param year Year
     * @return Flow emitting the total expenses for the month
     */
    @Query(
        """
        SELECT COALESCE(SUM(amount), 0.0) FROM transactions 
        WHERE type = 'EXPENSE'
        AND strftime('%m', dateTime / 1000, 'unixepoch') = :month 
        AND strftime('%Y', dateTime / 1000, 'unixepoch') = :year
        """
    )
    fun getMonthlyExpense(month: String, year: String): Flow<Double>

    /**
     * Get daily spending for each day in a specific month.
     *
     * @param month Month (1-12)
     * @param year Year
     * @return Flow emitting daily totals for each day
     */
    @Query(
        """
        SELECT 
            DATE(dateTime / 1000, 'unixepoch') as day,
            COALESCE(SUM(amount), 0.0) as total
        FROM transactions 
        WHERE type = 'EXPENSE'
        AND strftime('%m', dateTime / 1000, 'unixepoch') = :month 
        AND strftime('%Y', dateTime / 1000, 'unixepoch') = :year
        GROUP BY DATE(dateTime / 1000, 'unixepoch')
        ORDER BY day ASC
        """
    )
    fun getDailyExpenseForMonth(month: String, year: String): Flow<List<Map<String, Any>>>

    /**
     * Delete all transactions (used for testing or data reset).
     */
    @Query("DELETE FROM transactions")
    suspend fun deleteAll()
}

