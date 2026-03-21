package com.example.expencetracker.domain.repository

import androidx.paging.PagingData
import com.example.expencetracker.domain.model.Transaction
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for transaction data access.
 * This abstraction separates domain logic from data implementation.
 */
interface TransactionRepository {

    /**
     * Add a new transaction.
     *
     * @param transaction The transaction to add
     * @return The ID of the newly added transaction
     */
    suspend fun addTransaction(transaction: Transaction): Long

    /**
     * Update an existing transaction.
     *
     * @param transaction The transaction with updated data
     */
    suspend fun updateTransaction(transaction: Transaction)

    /**
     * Delete a transaction.
     *
     * @param transactionId The ID of the transaction to delete
     */
    suspend fun deleteTransaction(transactionId: Long)

    /**
     * Get a transaction by ID.
     *
     * @param transactionId The ID of the transaction
     * @return Flow emitting the transaction, or null if not found
     */
    fun getTransactionById(transactionId: Long): Flow<Transaction?>

    /**
     * Get all transactions.
     *
     * @return Flow emitting all transactions
     */
    fun getAllTransactions(): Flow<List<Transaction>>

    /**
     * Get paginated transactions.
     *
     * @return Flow emitting paginated transaction data
     */
    fun getPaginatedTransactions(): Flow<PagingData<Transaction>>

    /**
     * Get transactions filtered by type and date range.
     *
     * @param type Transaction type (INCOME or EXPENSE)
     * @param startDate Start date in epoch milliseconds
     * @param endDate End date in epoch milliseconds
     * @return Flow emitting filtered transactions
     */
    fun getFilteredTransactions(
        type: String,
        startDate: Long,
        endDate: Long
    ): Flow<List<Transaction>>

    /**
     * Get transactions by category.
     *
     * @param categoryId The category ID
     * @return Flow emitting transactions of the category
     */
    fun getTransactionsByCategory(categoryId: Long): Flow<List<Transaction>>

    /**
     * Search transactions by note.
     *
     * @param query The search query
     * @return Flow emitting matching transactions
     */
    fun searchTransactions(query: String): Flow<List<Transaction>>

    /**
     * Get total income for a specific month.
     *
     * @param month Month (1-12)
     * @param year Year
     * @return Flow emitting the total income
     */
    fun getMonthlyIncome(month: Int, year: Int): Flow<Double>

    /**
     * Get total expenses for a specific month.
     *
     * @param month Month (1-12)
     * @param year Year
     * @return Flow emitting the total expenses
     */
    fun getMonthlyExpense(month: Int, year: Int): Flow<Double>

    /**
     * Get total spent on a category in a specific month.
     *
     * @param categoryId The category ID
     * @param month Month (1-12)
     * @param year Year
     * @return Flow emitting the total spent
     */
    fun getMonthlyExpenseByCategory(
        categoryId: Long,
        month: Int,
        year: Int
    ): Flow<Double>
}

