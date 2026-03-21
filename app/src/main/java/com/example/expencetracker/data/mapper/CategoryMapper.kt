package com.example.expencetracker.data.mapper

import com.example.expencetracker.data.local.entity.CategoryEntity
import com.example.expencetracker.domain.model.Category
import com.example.expencetracker.domain.model.CategoryType

/**
 * Mapper for converting between CategoryEntity and Category domain model.
 */
object CategoryMapper {

    /**
     * Map CategoryEntity to Category domain model.
     *
     * @param entity The category entity
     * @return The domain model category
     */
    fun mapToDomain(entity: CategoryEntity): Category {
        return Category(
            id = entity.id,
            name = entity.name,
            iconName = entity.iconName,
            colorHex = entity.colorHex,
            type = CategoryType.valueOf(entity.type)
        )
    }

    /**
     * Map Category domain model to CategoryEntity.
     *
     * @param domain The domain model category
     * @return The category entity
     */
    fun mapToEntity(domain: Category): CategoryEntity {
        return CategoryEntity(
            id = domain.id,
            name = domain.name,
            iconName = domain.iconName,
            colorHex = domain.colorHex,
            type = domain.type.name
        )
    }
}

