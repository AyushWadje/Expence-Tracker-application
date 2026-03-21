================================================================================
VIEWMODEL QUICK REFERENCE GUIDE
================================================================================

This document provides a quick reference for using the ViewModels in the
Expense Tracker application.

================================================================================
DASHBOARD VIEW MODEL
================================================================================

Class: DashboardViewModel
Location: presentation/viewmodel/DashboardViewModel.kt

State Type: DashboardUiState
Events: DashboardEvent

Properties:
- uiState: StateFlow<DashboardUiState>
- selectedMonth: StateFlow<Int>
- selectedYear: StateFlow<Int>

Data in UiState:
- isLoading: Boolean
- totalIncome: Double
- totalExpense: Double
- recentTransactions: List<Transaction>
- categoryBreakdown: List<CategoryStat>
- budgetStatus: List<BudgetStatus>
- errorMessage: String?

Events:
- DashboardEvent.SelectMonth(month: Int)
- DashboardEvent.SelectYear(year: Int)
- DashboardEvent.Refresh

Usage in Composable:
```kotlin
val viewModel = hiltViewModel<DashboardViewModel>()
val state by viewModel.uiState.collectAsState()
val month by viewModel.selectedMonth.collectAsState()
val year by viewModel.selectedYear.collectAsState()

viewModel.onEvent(DashboardEvent.SelectMonth(3))
viewModel.onEvent(DashboardEvent.SelectYear(2024))
```

================================================================================
TRANSACTIONS VIEW MODEL
================================================================================

Class: TransactionsViewModel
Location: presentation/viewmodel/TransactionsViewModel.kt

State Type: TransactionsUiState
Events: TransactionsEvent

Properties:
- uiState: StateFlow<TransactionsUiState>
- searchQuery: StateFlow<String>

Data in UiState:
- transactions: PagingData<Transaction>
- categories: List<Category>
- isLoading: Boolean
- errorMessage: String?
- undoMessage: String?

Events:
- TransactionsEvent.SearchQuery(query: String)
- TransactionsEvent.DeleteTransaction(transactionId: Long)
- TransactionsEvent.FilterByType(type: String)
- TransactionsEvent.ClearFilter

Usage in Composable:
```kotlin
val viewModel = hiltViewModel<TransactionsViewModel>()
val state by viewModel.uiState.collectAsState()
val query by viewModel.searchQuery.collectAsState()

viewModel.onEvent(TransactionsEvent.SearchQuery("groceries"))
viewModel.onEvent(TransactionsEvent.FilterByType("EXPENSE"))
viewModel.onEvent(TransactionsEvent.DeleteTransaction(1L))
```

Paging Usage:
```kotlin
val transactions = state.transactions.collectAsLazyPagingItems()
LazyColumn {
    items(transactions.itemCount) { index ->
        val transaction = transactions[index]
        // Display transaction
    }
}
```

================================================================================
ADD/EDIT TRANSACTION VIEW MODEL
================================================================================

Class: AddEditTransactionViewModel
Location: presentation/viewmodel/AddEditTransactionViewModel.kt

State Type: AddEditUiState
Events: AddEditEvent

Methods:
- initialize(transactionId: Long?): Initialize for add or edit
- onEvent(event: AddEditEvent): Handle user interactions

Data in UiState:
- transactionId: Long?
- amount: String
- type: TransactionType
- selectedCategory: Category?
- categories: List<Category>
- dateTime: Long
- paymentMethod: PaymentMethod
- note: String
- receiptImageUri: String?
- isLoading: Boolean
- errorMessage: String?
- savedSuccessfully: Boolean

Events:
- AddEditEvent.UpdateAmount(amount: String)
- AddEditEvent.UpdateType(type: TransactionType)
- AddEditEvent.SelectCategory(category: Category)
- AddEditEvent.UpdateDateTime(dateTime: Long)
- AddEditEvent.UpdatePaymentMethod(method: PaymentMethod)
- AddEditEvent.UpdateNote(note: String)
- AddEditEvent.UpdateReceiptUri(uri: String)
- AddEditEvent.Save

Usage in Composable:
```kotlin
val viewModel = hiltViewModel<AddEditTransactionViewModel>()

LaunchedEffect(transactionId) {
    viewModel.initialize(transactionId)
}

val state by viewModel.uiState.collectAsState()

TextField(
    value = state.amount,
    onValueChange = { 
        viewModel.onEvent(AddEditEvent.UpdateAmount(it)) 
    }
)

Button(onClick = {
    viewModel.onEvent(AddEditEvent.Save)
})
```

================================================================================
REPORTS VIEW MODEL
================================================================================

Class: ReportsViewModel
Location: presentation/viewmodel/ReportsViewModel.kt

State Type: ReportsUiState
Events: ReportsEvent

Properties:
- uiState: StateFlow<ReportsUiState>
- selectedMonth: StateFlow<YearMonth>

Data in UiState:
- categoryBreakdown: List<CategoryStat>
- dailyStats: List<DailyStat>
- totalIncome: Double
- totalExpense: Double
- isLoading: Boolean
- errorMessage: String?
- exportSuccessful: Boolean

Events:
- ReportsEvent.SelectMonth(yearMonth: YearMonth)
- ReportsEvent.ExportReport

Usage in Composable:
```kotlin
val viewModel = hiltViewModel<ReportsViewModel>()
val state by viewModel.uiState.collectAsState()
val month by viewModel.selectedMonth.collectAsState()

// Display category breakdown pie chart
state.categoryBreakdown.forEach { stat ->
    // stat.category.name
    // stat.amount
    // stat.percentage
}

// Display daily stats line chart
state.dailyStats.forEach { daily ->
    // daily.day
    // daily.total
}

viewModel.onEvent(ReportsEvent.SelectMonth(YearMonth.now().minusMonths(1)))
viewModel.onEvent(ReportsEvent.ExportReport)
```

================================================================================
BUDGETS VIEW MODEL
================================================================================

Class: BudgetsViewModel
Location: presentation/viewmodel/BudgetsViewModel.kt

State Type: BudgetsUiState
Events: BudgetsEvent

Properties:
- uiState: StateFlow<BudgetsUiState>
- selectedMonth: StateFlow<Int>
- selectedYear: StateFlow<Int>

Data in UiState:
- budgetStatuses: List<BudgetStatus>
- categories: List<Category>
- isLoading: Boolean
- errorMessage: String?
- successMessage: String?

Events:
- BudgetsEvent.SelectMonth(month: Int)
- BudgetsEvent.SelectYear(year: Int)
- BudgetsEvent.SetBudget(categoryId: Long, limit: Double)
- BudgetsEvent.DeleteBudget(budgetId: Long)

Data in BudgetStatus:
- category: Category
- spent: Double
- limit: Double
- percentage: Float (calculated)
- remaining: Double (calculated)

Usage in Composable:
```kotlin
val viewModel = hiltViewModel<BudgetsViewModel>()
val state by viewModel.uiState.collectAsState()

// Display budget progress
state.budgetStatuses.forEach { status ->
    BudgetProgressBar(budgetStatus = status)
}

// Set a new budget
viewModel.onEvent(BudgetsEvent.SetBudget(
    categoryId = 1L,
    limit = 5000.0
))

// Delete a budget
viewModel.onEvent(BudgetsEvent.DeleteBudget(budgetId = 1L))
```

Progress Color Coding:
- Green: < 60% of budget
- Yellow: 60% - 85% of budget
- Orange: 85% - 100% of budget
- Red: > 100% of budget

================================================================================
SETTINGS VIEW MODEL
================================================================================

Class: SettingsViewModel
Location: presentation/viewmodel/SettingsViewModel.kt

State Type: SettingsUiState
Events: SettingsEvent

Data in UiState:
- currency: String (e.g., "INR", "USD", "EUR")
- themeMode: String (e.g., "SYSTEM", "LIGHT", "DARK")
- biometricEnabled: Boolean
- budgetResetDay: Int (1-28)
- notificationsEnabled: Boolean
- appVersion: String
- errorMessage: String?

Events:
- SettingsEvent.UpdateCurrency(currency: String)
- SettingsEvent.UpdateThemeMode(mode: String)
- SettingsEvent.UpdateBiometricEnabled(enabled: Boolean)
- SettingsEvent.UpdateBudgetResetDay(day: Int)
- SettingsEvent.UpdateNotificationsEnabled(enabled: Boolean)

Supported Currencies:
- INR (Indian Rupee)
- USD (US Dollar)
- EUR (Euro)
- GBP (British Pound)
- JPY (Japanese Yen)

Usage in Composable:
```kotlin
val viewModel = hiltViewModel<SettingsViewModel>()
val state by viewModel.uiState.collectAsState()

// Update currency
DropdownMenu(
    selected = state.currency,
    onSelect = { currency ->
        viewModel.onEvent(SettingsEvent.UpdateCurrency(currency))
    }
)

// Update theme
RadioButton(
    selected = state.themeMode == "DARK",
    onClick = {
        viewModel.onEvent(SettingsEvent.UpdateThemeMode("DARK"))
    }
)

// Toggle biometric
Switch(
    checked = state.biometricEnabled,
    onCheckedChange = { enabled ->
        viewModel.onEvent(SettingsEvent.UpdateBiometricEnabled(enabled))
    }
)

// Set budget reset day
Slider(
    value = state.budgetResetDay.toFloat(),
    onValueChange = { day ->
        viewModel.onEvent(SettingsEvent.UpdateBudgetResetDay(day.toInt()))
    },
    valueRange = 1f..28f
)
```

================================================================================
COMMON PATTERNS
================================================================================

1. COLLECTING STATE:
```kotlin
val viewModel = hiltViewModel<YourViewModel>()
val state by viewModel.uiState.collectAsState()
```

2. HANDLING LOADING:
```kotlin
if (state.isLoading) {
    LoadingShimmer()
} else {
    // Display content
}
```

3. DISPLAYING ERRORS:
```kotlin
if (state.errorMessage != null) {
    Text(
        text = state.errorMessage,
        color = Color.Red
    )
}
```

4. SENDING EVENTS:
```kotlin
Button(onClick = {
    viewModel.onEvent(YourEvent.DoSomething(...))
})
```

5. INITIALIZATION IN COMPOSABLE:
```kotlin
LaunchedEffect(Unit) {
    viewModel.initialize()  // if needed
}
```

================================================================================
TESTING VIEWMODELS
================================================================================

Example Unit Test:
```kotlin
@Test
fun testSelectMonth() = runTest {
    val viewModel = DashboardViewModel(...)
    
    viewModel.onEvent(DashboardEvent.SelectMonth(3))
    
    viewModel.selectedMonth.test {
        val month = awaitItem()
        assert(month == 3)
    }
}
```

Use Turbine for Flow testing:
```kotlin
viewModel.uiState.test {
    val state = awaitItem()
    assert(state.isLoading == false)
}
```

================================================================================
DEBUGGING TIPS
================================================================================

1. Use `collectAsState()` to observe StateFlow changes
2. Add Log.d() statements in ViewModel events
3. Check Logcat for ViewModel initialization
4. Use Android Studio's debugger breakpoints
5. Check stacktraces for null pointer exceptions
6. Verify all use cases are properly injected by Hilt
7. Test with sample data before connecting to database

================================================================================

