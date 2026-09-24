#  BudgetApp v3.0

A modern, "brutally honest" budget tracking Android application designed specifically for the Kenyan university student lifestyle. 

BudgetApp doesn't just track your KES; it coaches you, predicts your financial survival, and gives you the hard truth when you're spending like a billionaire on a comrade's budget.

![Platform](https://img.shields.io/badge/Platform-Android-green)
![Language](https://img.shields.io/badge/Language-Kotlin-orange)
![Design](https://img.shields.io/badge/Design-Material%203-blue)
![Database](https://img.shields.io/badge/Database-Room-lightgrey)

---

## New in v3.0: The "Financial Sensei" Update

The latest version transforms BudgetApp from a simple tracker into a proactive financial companion:

*   ** Money Sensei (AI Coach)**: Consult your personal sensei for data-driven intelligence on your spending habits.
*   ** Survival Forecast**: A predictive engine that calculates your wallet's "life expectancy" based on your current burn rate.
*   ** Dynamic Themes**: Full support for Light, Dark, and System modes with a beautiful custom-patterned UI.
*   ** Category Guardrails**: Set specific monthly limits for Food, Transport, Fun, and more to stop overspending before it happens.
*   ** Animated Interface**: Smooth balance counters and interactive progress indicators for a premium feel.

---

## 📱 Core Screens

| Screen | Key Features |
|--------|-------------|
| ** Dashboard** | Live animated balance, Survival Forecast card, Funny Sensei messages, and Category Limit progress. |
| ** Quick Add** | Ultra-fast logging for "Comrade Essentials" (Food, Fare, Books, Coffee) directly from Home. |
| ** AI Coach** | Deep-dive conversation with the Money Sensei to fix your broken finances. |
| ** History** | Full searchable transaction log with quick-delete and category filtering. |
| ** Analytics** | Visual spending breakdown by category and time (Week, Month, Semester). |
| ** Settings** | Profile management, granular category budget limits, and theme selection. |

---

## Tech Stack & Architecture

BudgetApp follows modern Android development best practices and the **Clean Architecture** principles.

*   **Language**: Kotlin 2.0+
*   **UI Engine**: XML Layouts + Material Design 3 (M3) Components.
*   **Asynchronous Logic**: Kotlin Coroutines & Flow for non-blocking database operations.
*   **Data Persistence**: Room Database (SQLite) with multi-version migrations.
*   **Networking**: Retrofit & OkHttp (for AI Coach intelligence).
*   **Background Tasks**: WorkManager for budget threshold notifications.
*   **Navigation**: Jetpack Navigation Component with safe-args.
*   **Architecture**: MVVM (Model-View-ViewModel) with Repository Pattern.

---

##  Project Organization

```text
com.anthonykamau.budgetapp/
├── data/
│   ├── dao/          # Room Access Objects
│   ├── db/           # Database Configuration & Migrations
│   ├── entity/       # Data Models (Expense, Category, ChatMessage)
│   └── repository/   # Data source abstraction layer
├── ui/
│   ├── dashboard/    # Home & Summary logic
│   ├── aicoach/      # AI Intelligence & Chat UI
│   ├── analytics/    # Data visualization
│   ├── addexpense/   # Transaction input
│   └── settings/     # Configuration & Themes
└── util/             # Helpers (Calculators, Formatters, Custom Views)
```

---

##  Developer

**Anthony Kamau**  
*BBIT Third Year — Strathmore University*  
Mobile Application Development Project

---

## 📄 License

This project is open-source under the **MIT License**. Feel free to fork and improve!
