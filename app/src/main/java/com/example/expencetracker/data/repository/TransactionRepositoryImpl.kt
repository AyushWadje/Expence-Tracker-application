package com.example.expencetracker.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.expencetracker.data.local.dao.TransactionDao
import com.example.expencetracker.data.local.dao.CategoryDao
import com.example.expencetracker.data.mapper.TransactionMapper
import com.example.expencetracker.data.mapper.CategoryMapper
import com.example.expencetracker.domain.model.Transaction
import com.example.expencetracker.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Implementation of TransactionRepository.
 * Handles all data access operations for transactions.
 */
class TransactionRepositoryImpl(
    private val transactionDao: TransactionDao,
    private val categoryDao: CategoryDao
) : TransactionRepository {

    override suspend fun addTransaction(transaction: Transaction): Long {
        val entity = TransactionMapper.mapToEntity(transaction)
        return transactionDao.insert(entity)
    }

    override suspend fun updateTransaction(transaction: Transaction) {
        val entity = TransactionMapper.mapToEntity(transaction)
        transactionDao.update(entity)
    }

    override suspend fun deleteTransaction(transactionId: Long) {
        val transaction = transactionDao.getTransactionById(transactionId)
        transaction.collect { entity ->
            if (entity != null) {
                transactionDao.delete(entity)
            }
        }
    }

    override fun getTransactionById(transactionId: Long): Flow<Transaction?> {
        return combine(
            transactionDao.getTransactionById(transactionId),
            categoryDao.getAllCategories()
        ) { entity, categories ->
            if (entity != null) {
                val categoryEntity = categories.find { it.id == entity.categoryId }
                if (categoryEntity != null) {
                    TransactionMapper.mapToDomain(
                        entity,
                        CategoryMapper.mapToDomain(categoryEntity)
                    )
                } else null
            } else null
        }
    }

    override fun getAllTransactions(): Flow<List<Transaction>> {
        return combine(
            transactionDao.getAllTransactions(),
            categoryDao.getAllCategories()
        ) { entities, categories ->
            entities.map { entity ->
                val category = categories.find { it.id == entity.categoryId }
                if (category != null) {
                    TransactionMapper.mapToDomain(entity, CategoryMapper.mapToDomain(category))
                } else null
            }.filterNotNull()
        }
    }

    override fun getPaginatedTransactions(): Flow<PagingData<Transaction>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { transactionDao.getPaginatedTransactions() }
        ).flow.combine(categoryDao.getAllCategories()) { pagingData, categories ->
            pagingData.map { entity ->
                val category = categories.find { it.id == entity.categoryId }
                if (category != null) {
                    TransactionMapper.mapToDomain(entity, CategoryMapper.mapToDomain(category))
                } else throw IllegalStateException("Category not found for transaction")
            }
        }
    }

    override fun getFilteredTransactions(
        type: String,
        startDate: Long,
        endDate: Long
    ): Flow<List<Transaction>> {
        return combine(
            transactionDao.getTransactionsByTypeAndDateRange(type, startDate, endDate),
            categoryDao.getAllCategories()
        ) { entities, categories ->
            entities.map { entity ->
                val category = categories.find { it.id == entity.categoryId }
                if (category != null) {
                    TransactionMapper.mapToDomain(entity, CategoryMapper.mapToDomain(category))
                } else null
            }.filterNotNull()
        }
    }

    override fun getTransactionsByCategory(categoryId: Long): Flow<List<Transaction>> {
        return combine(
            transactionDao.getTransactionsByCategory(categoryId),
            categoryDao.getCategoryById(categoryId)
        ) { entities, category ->
            if (category != null) {
                entities.map { entity ->
                    TransactionMapper.mapToDomain(entity, CategoryMapper.mapToDomain(category))
                }
            } else emptyList()
        }
    }

    override fun searchTransactions(query: String): Flow<List<Transaction>> {
        return combine(
            transactionDao.searchTransactions(query),
            categoryDao.getAllCategories()
        ) { entities, categories ->
            entities.map { entity ->
                val category = categories.find { it.id == entity.categoryId }
                if (category != null) {
                    TransactionMapper.mapToDomain(entity, CategoryMapper.mapToDomain(category))
                } else null
            }.filterNotNull()
        }
    }

    override fun getMonthlyIncome(month: Int, year: Int): Flow<Double> {
        val monthStr = month.toString().padStart(2, '0')
        val yearStr = year.toString()
        return transactionDao.getMonthlyIncome(monthStr, yearStr)
    }

    override fun getMonthlyExpense(month: Int, year: Int): Flow<Double> {
        val monthStr = month.toString().padStart(2, '0')
        val yearStr = year.toString()
        return transactionDao.getMonthlyExpense(monthStr, yearStr)
    }

    override fun getMonthlyExpenseByCategory(
        categoryId: Long,
        month: Int,
        year: Int
    ): Flow<Double> {
        val monthStr = month.toString().padStart(2, '0')
        val yearStr = year.toString()
        return transactionDao.getMonthlyExpenseByCategory(categoryId, monthStr, yearStr)
    }
}

