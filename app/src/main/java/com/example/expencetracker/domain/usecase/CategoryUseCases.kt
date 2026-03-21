package com.example.expencetracker.domain.usecase

import com.example.expencetracker.domain.model.Category
import com.example.expencetracker.domain.repository.CategoryRepository

/**
 * Use case for adding a new category.
 */
class AddCategoryUseCase(private val repository: CategoryRepository) {
    suspend operator fun invoke(category: Category): Long {
        return repository.addCategory(category)
    }
}

/**
 * Use case for updating an existing category.
 */
class UpdateCategoryUseCase(private val repository: CategoryRepository) {
    suspend operator fun invoke(category: Category) {
        repository.updateCategory(category)
    }
}

/**
 * Use case for deleting a category.
 */
class DeleteCategoryUseCase(private val repository: CategoryRepository) {
    suspend operator fun invoke(categoryId: Long) {
        repository.deleteCategory(categoryId)
    }
}

/**
 * Use case for getting a category by ID.
 */
class GetCategoryByIdUseCase(private val repository: CategoryRepository) {
    operator fun invoke(categoryId: Long) = repository.getCategoryById(categoryId)
}

/**
 * Use case for getting all categories.
 */
class GetAllCategoriesUseCase(private val repository: CategoryRepository) {
    operator fun invoke() = repository.getAllCategories()
}

/**
 * Use case for getting categories by type.
 */
class GetCategoriesByTypeUseCase(private val repository: CategoryRepository) {
    operator fun invoke(type: String) = repository.getCategoriesByType(type)
}

