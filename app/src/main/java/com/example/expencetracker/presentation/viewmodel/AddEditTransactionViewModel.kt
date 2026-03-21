package com.example.expencetracker.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expencetracker.domain.model.Transaction
import com.example.expencetracker.domain.model.Category
import com.example.expencetracker.domain.model.TransactionType
import com.example.expencetracker.domain.model.PaymentMethod
import com.example.expencetracker.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Add/Edit Transaction screen.
 * Manages the state and logic for adding or updating transactions.
 */
@HiltViewModel
class AddEditTransactionViewModel @Inject constructor(
    private val addTransactionUseCase: AddTransactionUseCase,
    private val updateTransactionUseCase: UpdateTransactionUseCase,
    private val getTransactionByIdUseCase: GetTransactionByIdUseCase,
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<AddEditUiState>(AddEditUiState())
    val uiState: StateFlow<AddEditUiState> = _uiState.asStateFlow()

    /**
     * Initialize the screen with an optional transaction ID for editing.
     */
    fun initialize(transactionId: Long?) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                // Load categories
                getAllCategoriesUseCase().collect { categories ->
                    _uiState.value = _uiState.value.copy(
                        categories = categories
                    )
                }

                // Load transaction if editing
                if (transactionId != null) {
                    getTransactionByIdUseCase(transactionId).collect { transaction ->
                        if (transaction != null) {
                            _uiState.value = _uiState.value.copy(
                                transactionId = transaction.id,
                                amount = transaction.amount.toString(),
                                type = transaction.type,
                                selectedCategory = transaction.category,
                                dateTime = transaction.dateTime,
                                paymentMethod = transaction.paymentMethod,
                                note = transaction.note,
                                isLoading = false
                            )
                        }
                    }
                } else {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message
                )
            }
        }
    }

    /**
     * Handle events on the add/edit screen.
     */
    fun onEvent(event: AddEditEvent) {
        when (event) {
            is AddEditEvent.UpdateAmount -> {
                _uiState.value = _uiState.value.copy(amount = event.amount)
            }
            is AddEditEvent.UpdateType -> {
                _uiState.value = _uiState.value.copy(type = event.type)
            }
            is AddEditEvent.SelectCategory -> {
                _uiState.value = _uiState.value.copy(selectedCategory = event.category)
            }
            is AddEditEvent.UpdateDateTime -> {
                _uiState.value = _uiState.value.copy(dateTime = event.dateTime)
            }
            is AddEditEvent.UpdatePaymentMethod -> {
                _uiState.value = _uiState.value.copy(paymentMethod = event.method)
            }
            is AddEditEvent.UpdateNote -> {
                _uiState.value = _uiState.value.copy(note = event.note)
            }
            is AddEditEvent.UpdateReceiptUri -> {
                _uiState.value = _uiState.value.copy(receiptImageUri = event.uri)
            }
            AddEditEvent.Save -> {
                saveTransaction()
            }
        }
    }

    /**
     * Save or update the transaction.
     */
    private fun saveTransaction() {
        viewModelScope.launch {
            try {
                val state = _uiState.value
                val amountDouble = state.amount.toDoubleOrNull()
                
                if (amountDouble == null || amountDouble <= 0) {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Please enter a valid amount"
                    )
                    return@launch
                }

                if (state.selectedCategory == null) {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Please select a category"
                    )
                    return@launch
                }

                val transaction = Transaction(
                    id = state.transactionId ?: 0,
                    amount = amountDouble,
                    type = state.type,
                    category = state.selectedCategory,
                    note = state.note,
                    dateTime = state.dateTime,
                    paymentMethod = state.paymentMethod,
                    receiptImageUri = state.receiptImageUri
                )

                if (state.transactionId == null) {
                    addTransactionUseCase(transaction)
                } else {
                    updateTransactionUseCase(transaction)
                }

                _uiState.value = _uiState.value.copy(
                    savedSuccessfully = true
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message
                )
            }
        }
    }
}

/**
 * UI State for the Add/Edit screen.
 */
data class AddEditUiState(
    val transactionId: Long? = null,
    val amount: String = "",
    val type: TransactionType = TransactionType.EXPENSE,
    val selectedCategory: Category? = null,
    val categories: List<Category> = emptyList(),
    val dateTime: Long = System.currentTimeMillis(),
    val paymentMethod: PaymentMethod = PaymentMethod.CARD,
    val note: String = "",
    val receiptImageUri: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val savedSuccessfully: Boolean = false
)

/**
 * Events for the Add/Edit screen.
 */
sealed class AddEditEvent {
    data class UpdateAmount(val amount: String) : AddEditEvent()
    data class UpdateType(val type: TransactionType) : AddEditEvent()
    data class SelectCategory(val category: Category) : AddEditEvent()
    data class UpdateDateTime(val dateTime: Long) : AddEditEvent()
    data class UpdatePaymentMethod(val method: PaymentMethod) : AddEditEvent()
    data class UpdateNote(val note: String) : AddEditEvent()
    data class UpdateReceiptUri(val uri: String) : AddEditEvent()
    data object Save : AddEditEvent()
}

