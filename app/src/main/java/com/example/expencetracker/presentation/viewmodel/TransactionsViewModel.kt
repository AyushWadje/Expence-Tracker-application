package com.example.expencetracker.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.expencetracker.domain.model.Transaction
import com.example.expencetracker.domain.model.Category
import com.example.expencetracker.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Transactions screen.
 * Manages the state and logic for displaying and managing transactions.
 */
@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val getPagedTransactionsUseCase: GetPagedTransactionsUseCase,
    private val getFilteredTransactionsUseCase: GetFilteredTransactionsUseCase,
    private val searchTransactionsUseCase: SearchTransactionsUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase,
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<TransactionsUiState>(TransactionsUiState())
    val uiState: StateFlow<TransactionsUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        loadPaginatedTransactions()
        loadCategories()
    }

    /**
     * Load paginated transactions.
     */
    private fun loadPaginatedTransactions() {
        viewModelScope.launch {
            try {
                getPagedTransactionsUseCase().collect { pagingData ->
                    _uiState.value = _uiState.value.copy(
                        transactions = pagingData,
                        isLoading = false
                    )
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
     * Load all categories.
     */
    private fun loadCategories() {
        viewModelScope.launch {
            try {
                getAllCategoriesUseCase().collect { categories ->
                    _uiState.value = _uiState.value.copy(
                        categories = categories
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message
                )
            }
        }
    }

    /**
     * Handle events on the transactions screen.
     */
    fun onEvent(event: TransactionsEvent) {
        when (event) {
            is TransactionsEvent.SearchQuery -> {
                _searchQuery.value = event.query
                if (event.query.isNotEmpty()) {
                    searchTransactions(event.query)
                } else {
                    loadPaginatedTransactions()
                }
            }
            is TransactionsEvent.DeleteTransaction -> {
                deleteTransaction(event.transactionId)
            }
            is TransactionsEvent.FilterByType -> {
                filterByType(event.type)
            }
            TransactionsEvent.ClearFilter -> {
                loadPaginatedTransactions()
            }
        }
    }

    /**
     * Search transactions by query.
     */
    private fun searchTransactions(query: String) {
        viewModelScope.launch {
            try {
                searchTransactionsUseCase(query).collect { transactions ->
                    // Convert to PagingData - simplified for now
                    _uiState.value = _uiState.value.copy(
                        isLoading = false
                    )
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
     * Filter transactions by type.
     */
    private fun filterByType(type: String) {
        viewModelScope.launch {
            try {
                val now = System.currentTimeMillis()
                val startOfMonth = now - (30L * 24 * 60 * 60 * 1000)
                
                getFilteredTransactionsUseCase(type, startOfMonth, now).collect { _ ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false
                    )
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
     * Delete a transaction.
     */
    private fun deleteTransaction(transactionId: Long) {
        viewModelScope.launch {
            try {
                deleteTransactionUseCase(transactionId)
                _uiState.value = _uiState.value.copy(
                    undoMessage = "Transaction deleted"
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
 * UI State for the Transactions screen.
 */
data class TransactionsUiState(
    val transactions: PagingData<Transaction> = PagingData.empty(),
    val categories: List<Category> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val undoMessage: String? = null
)

/**
 * Events for the Transactions screen.
 */
sealed class TransactionsEvent {
    data class SearchQuery(val query: String) : TransactionsEvent()
    data class DeleteTransaction(val transactionId: Long) : TransactionsEvent()
    data class FilterByType(val type: String) : TransactionsEvent()
    data object ClearFilter : TransactionsEvent()
}

