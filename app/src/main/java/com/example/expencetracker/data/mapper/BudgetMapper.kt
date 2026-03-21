package com.example.expencetracker.data.mapper

import com.example.expencetracker.data.local.entity.BudgetEntity
import com.example.expencetracker.domain.model.Budget

/**
 * Mapper for converting between BudgetEntity and Budget domain model.
 */
object BudgetMapper {

    /**
     * Map BudgetEntity to Budget domain model.
     *
     * @param entity The budget entity
     * @return The domain model budget
     */
    fun mapToDomain(entity: BudgetEntity): Budget {
        return Budget(
            id = entity.id,
            categoryId = entity.categoryId,
            limitAmount = entity.limitAmount,
            month = entity.month,
            year = entity.year
        )
    }

    /**
     * Map Budget domain model to BudgetEntity.
     *
     * @param domain The domain model budget
     * @return The budget entity
     */
    fun mapToEntity(domain: Budget): BudgetEntity {
        return BudgetEntity(
            id = domain.id,
            categoryId = domain.categoryId,
            limitAmount = domain.limitAmount,
            month = domain.month,
            year = domain.year
        )
    }
}

