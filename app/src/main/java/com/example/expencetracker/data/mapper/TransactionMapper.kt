package com.example.expencetracker.data.mapper

import com.example.expencetracker.data.local.entity.TransactionEntity
import com.example.expencetracker.domain.model.Transaction
import com.example.expencetracker.domain.model.TransactionType
import com.example.expencetracker.domain.model.PaymentMethod
import com.example.expencetracker.domain.model.RecurringInterval
import com.example.expencetracker.domain.model.Category

/**
 * Mapper for converting between TransactionEntity and Transaction domain model.
 */
object TransactionMapper {

    /**
     * Map TransactionEntity to Transaction domain model.
     *
     * @param entity The transaction entity
     * @param category The associated category
     * @return The domain model transaction
     */
    fun mapToDomain(entity: TransactionEntity, category: Category): Transaction {
        return Transaction(
            id = entity.id,
            amount = entity.amount,
            type = TransactionType.valueOf(entity.type),
            category = category,
            note = entity.note,
            dateTime = entity.dateTime,
            paymentMethod = PaymentMethod.valueOf(entity.paymentMethod),
            receiptImageUri = entity.receiptImageUri,
            isRecurring = entity.isRecurring,
            recurringInterval = entity.recurringInterval?.let { RecurringInterval.valueOf(it) }
        )
    }

    /**
     * Map Transaction domain model to TransactionEntity.
     *
     * @param domain The domain model transaction
     * @return The transaction entity
     */
    fun mapToEntity(domain: Transaction): TransactionEntity {
        return TransactionEntity(
            id = domain.id,
            amount = domain.amount,
            type = domain.type.name,
            categoryId = domain.category.id,
            note = domain.note,
            dateTime = domain.dateTime,
            paymentMethod = domain.paymentMethod.name,
            receiptImageUri = domain.receiptImageUri,
            isRecurring = domain.isRecurring,
            recurringInterval = domain.recurringInterval?.name
        )
    }
}

