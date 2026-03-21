package com.example.expencetracker.data.repository

import com.example.expencetracker.data.local.dao.BudgetDao
import com.example.expencetracker.data.mapper.BudgetMapper
import com.example.expencetracker.domain.model.Budget
import com.example.expencetracker.domain.repository.BudgetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Implementation of BudgetRepository.
 * Handles all data access operations for budgets.
 */
class BudgetRepositoryImpl(
    private val budgetDao: BudgetDao
) : BudgetRepository {

    override suspend fun setBudget(budget: Budget): Long {
        val entity = BudgetMapper.mapToEntity(budget)
        return budgetDao.insert(entity)
    }

    override suspend fun updateBudget(budget: Budget) {
        val entity = BudgetMapper.mapToEntity(budget)
        budgetDao.update(entity)
    }

    override suspend fun deleteBudget(budgetId: Long) {
        val budget = budgetDao.getBudgetById(budgetId)
        budget.collect { entity ->
            if (entity != null) {
                budgetDao.delete(entity)
            }
        }
    }

    override fun getBudgetById(budgetId: Long): Flow<Budget?> {
        return budgetDao.getBudgetById(budgetId).map { entity ->
            entity?.let { BudgetMapper.mapToDomain(it) }
        }
    }

    override fun getAllBudgets(): Flow<List<Budget>> {
        return budgetDao.getAllBudgets().map { entities ->
            entities.map { BudgetMapper.mapToDomain(it) }
        }
    }

    override fun getBudgetsByMonthYear(month: Int, year: Int): Flow<List<Budget>> {
        return budgetDao.getBudgetsByMonthYear(month, year).map { entities ->
            entities.map { BudgetMapper.mapToDomain(it) }
        }
    }

    override fun getBudgetByCategory(categoryId: Long, month: Int, year: Int): Flow<Budget?> {
        return budgetDao.getBudgetByCategory(categoryId, month, year).map { entity ->
            entity?.let { BudgetMapper.mapToDomain(it) }
        }
    }
}

