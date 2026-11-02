# Expense Tracker

An Android expense tracking application built with Kotlin, Jetpack Compose, and Clean Architecture.

## Overview

Expense Tracker lets users record income and expenses, organize them by category, set monthly budgets, and review spending through reports. The data, domain, and dependency injection layers are complete. The Compose screens are scaffolded and ready for UI implementation (see [Project Status](#project-status)).

## Tech Stack

| Area | Technology |
|------|------------|
| Language | Kotlin |
| UI | Jetpack Compose, Material Design 3 |
| Architecture | Clean Architecture, MVVM |
| Database | Room 2.6.1 |
| Dependency Injection | Hilt 2.51 |
| Asynchrony | Coroutines, Flow, StateFlow |
| Navigation | Navigation Compose 2.7.7 |
| Pagination | Paging 3 |
| Preferences | DataStore 1.0.0 |
| Background Work | WorkManager 2.9.1 |
| Charts | Vico 1.15.0 |
| Logging | Timber 5.0.1 |
| Testing | JUnit 5, MockK, Turbine |

## Features

- **Dashboard:** monthly balance, income and expense summary, recent transactions, and budget status.
- **Transactions:** searchable, filterable list with swipe-to-delete and undo.
- **Add/Edit Transaction:** amount, type, category, date and time, payment method, and receipt attachment, with input validation.
- **Reports:** monthly, category, and daily charts, plus CSV export.
- **Budgets:** per-category monthly limits with progress indicators and daily alert notifications.
- **Settings:** currency (INR, USD, EUR, GBP, JPY), theme (Light, Dark, System), biometric lock, budget reset day, and notification preferences.

## Architecture

The project follows a three-layer Clean Architecture with MVVM in the presentation layer.

```
app/src/main/java/com/example/expencetracker/
├── data/            # Room database, DAOs, entities, mappers, repository implementations, DataStore
├── domain/          # Domain models, repository interfaces, use cases
├── presentation/    # ViewModels, Compose screens, reusable components, navigation
├── di/              # Hilt modules
├── ui/theme/        # Material 3 theme
├── util/            # Currency and date utilities
└── worker/          # WorkManager tasks
```

### Database Schema

| Table | Key Fields |
|-------|------------|
| `transactions` | id, amount, type, categoryId (FK), note, dateTime, paymentMethod, receiptImageUri, isRecurring, recurringInterval |
| `categories` | id, name, iconName, colorHex, type (10 defaults pre-populated) |
| `budgets` | id, categoryId (FK), limitAmount, month, year |

## Getting Started

### Prerequisites

- Android Studio Giraffe or newer
- JDK 11 or newer
- Android SDK: minimum API 24, target API 36
- Gradle 8.0 or newer

### Setup

1. Open the project in Android Studio.
2. Sync Gradle files.
3. Build the project.
4. Run on an emulator or physical device.

### Common Commands

```bash
./gradlew build                   # Build
./gradlew installDebug            # Install debug build
./gradlew test                    # Unit tests
./gradlew connectedAndroidTest    # Instrumentation tests
./gradlew clean                   # Clean
```

## Testing

- **Unit tests** (`app/src/test`): use cases, repositories, and ViewModels (MockK, Turbine).
- **Instrumentation tests** (`app/src/androidTest`): DAO tests using an in-memory Room database.

## Project Status

**Complete**

- Data layer (Room, DataStore, repositories, mappers)
- Domain layer (models, repository interfaces, 23 use cases)
- Hilt dependency injection
- Six ViewModels with StateFlow-based state
- Navigation, Material 3 theming, and reusable components
- Unit and integration tests

**In progress**

- UI implementation for the Dashboard, Transactions, Add/Edit, Reports, Budgets, and Settings screens

Implementation steps for the remaining screens are in `SCREEN_IMPLEMENTATION_GUIDE.md`.

## Documentation

| File | Contents |
|------|----------|
| `PROJECT_STRUCTURE.md` | Architecture overview |
| `VIEWMODEL_GUIDE.md` | ViewModel reference |
| `SCREEN_IMPLEMENTATION_GUIDE.md` | UI implementation steps |

## License

Provided as-is for educational and commercial use.

*Last updated: March 14, 2026*
