package com.example.expencetracker.domain.usecase

import com.example.expencetracker.domain.model.Transaction
import com.example.expencetracker.domain.repository.TransactionRepository

/**
 * Use case for adding a new transaction.
 */
class AddTransactionUseCase(private val repository: TransactionRepository) {
    suspend operator fun invoke(transaction: Transaction): Long {
        return repository.addTransaction(transaction)
    }
}

/**
 * Use case for updating an existing transaction.
 */
class UpdateTransactionUseCase(private val repository: TransactionRepository) {
    suspend operator fun invoke(transaction: Transaction) {
        repository.updateTransaction(transaction)
    }
}

/**
 * Use case for deleting a transaction.
 */
class DeleteTransactionUseCase(private val repository: TransactionRepository) {
    suspend operator fun invoke(transactionId: Long) {
        repository.deleteTransaction(transactionId)
    }
}

/**
 * Use case for getting a transaction by ID.
 */
class GetTransactionByIdUseCase(private val repository: TransactionRepository) {
    operator fun invoke(transactionId: Long) =
        repository.getTransactionById(transactionId)
}

/**
 * Use case for getting all transactions.
 */
class GetAllTransactionsUseCase(private val repository: TransactionRepository) {
    operator fun invoke() = repository.getAllTransactions()
}

/**
 * Use case for getting paginated transactions.
 */
class GetPagedTransactionsUseCase(private val repository: TransactionRepository) {
    operator fun invoke() = repository.getPaginatedTransactions()
}

/**
 * Use case for getting filtered transactions.
 */
class GetFilteredTransactionsUseCase(private val repository: TransactionRepository) {
    operator fun invoke(type: String, startDate: Long, endDate: Long) =
        repository.getFilteredTransactions(type, startDate, endDate)
}

/**
 * Use case for searching transactions.
 */
class SearchTransactionsUseCase(private val repository: TransactionRepository) {
    operator fun invoke(query: String) = repository.searchTransactions(query)
}

/**
 * Use case for getting transactions by category.
 */
class GetTransactionsByCategoryUseCase(private val repository: TransactionRepository) {
    operator fun invoke(categoryId: Long) = repository.getTransactionsByCategory(categoryId)
}

/**
 * Use case for getting monthly summary.
 */
class GetMonthlySummaryUseCase(private val repository: TransactionRepository) {
    operator fun invoke(month: Int, year: Int) = Pair(
        repository.getMonthlyIncome(month, year),
        repository.getMonthlyExpense(month, year)
    )
}

