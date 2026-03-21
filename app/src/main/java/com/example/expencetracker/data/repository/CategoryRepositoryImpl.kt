package com.example.expencetracker.data.repository

import com.example.expencetracker.data.local.dao.CategoryDao
import com.example.expencetracker.data.mapper.CategoryMapper
import com.example.expencetracker.domain.model.Category
import com.example.expencetracker.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Implementation of CategoryRepository.
 * Handles all data access operations for categories.
 */
class CategoryRepositoryImpl(
    private val categoryDao: CategoryDao
) : CategoryRepository {

    override suspend fun addCategory(category: Category): Long {
        val entity = CategoryMapper.mapToEntity(category)
        return categoryDao.insert(entity)
    }

    override suspend fun updateCategory(category: Category) {
        val entity = CategoryMapper.mapToEntity(category)
        categoryDao.update(entity)
    }

    override suspend fun deleteCategory(categoryId: Long) {
        val category = categoryDao.getCategoryById(categoryId)
        category.collect { entity ->
            if (entity != null) {
                categoryDao.delete(entity)
            }
        }
    }

    override fun getCategoryById(categoryId: Long): Flow<Category?> {
        return categoryDao.getCategoryById(categoryId).map { entity ->
            entity?.let { CategoryMapper.mapToDomain(it) }
        }
    }

    override fun getAllCategories(): Flow<List<Category>> {
        return categoryDao.getAllCategories().map { entities ->
            entities.map { CategoryMapper.mapToDomain(it) }
        }
    }

    override fun getCategoriesByType(type: String): Flow<List<Category>> {
        return categoryDao.getCategoriesByType(type).map { entities ->
            entities.map { CategoryMapper.mapToDomain(it) }
        }
    }
}

