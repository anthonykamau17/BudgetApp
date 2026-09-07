#  BudgetApp

A student budget tracking Android app built for Kenyan university students.
Track expenses, set savings goals, and get brutally honest (and funny) financial insights.

![Platform](https://img.shields.io/badge/Platform-Android-green)
![Language](https://img.shields.io/badge/Language-Kotlin-orange)
![Min SDK](https://img.shields.io/badge/Min%20SDK-26-blue)

---

## 📱 Screens

| Screen | Description |
|--------|-------------|
| 🏠 Dashboard | Live balance, progress bar, quick add, funny message |
| ➕ Add Expense | Custom keypad, 8 categories, under 3 seconds |
| 📋 History | Full expense list, search, filter, delete |
| 📊 Analytics | Bar chart, category breakdown, week/month/semester |
| 🎯 Goals | Savings goals with progress tracking |
| ⚙️ Settings | Budget setup, notifications, data management |

---

##  Features

- **Real-time dashboard** — remaining budget updates instantly when you log an expense
- **Quick Add** — tap Food, Fare, Books or Coffee from the home screen, pre-selected
- **Funny messages** — brutally honest spending insights in Kenyan student language
- **Money prediction** — tells you how many days your money will last at current spend rate
- **Budget warnings** — notifications at 80% and 100% spend
- **Savings goals** — track progress toward multiple goals simultaneously
- **Analytics** — see spending by day and by category across Week, Month or Semester
- **Onboarding** — 4-slide tutorial for first-time users
- **Splash screen** — branded launch screen

---

##  Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Kotlin |
| UI | XML Layouts + Material Design 3 |
| Architecture | MVVM + Repository Pattern |
| Database | Room (SQLite) |
| Async | Kotlin Coroutines + LiveData |
| Navigation | Jetpack Navigation Component |
| Background | WorkManager |
| Build | Gradle with Version Catalog |

---

## 🏗️ Project Structure
com.anthonykamau.budgetapp/
├── data/
│ ├── dao/ # Room DAOs
│ ├── db/ # AppDatabase
│ ├── entity/ # Room entities
│ └── repository/ # Repository layer
├── ui/
│ ├── dashboard/ # Home screen
│ ├── addexpense/ # Add expense screen
│ ├── history/ # Transaction history
│ ├── analytics/ # Charts & insights
│ ├── goals/ # Savings goals
│ └── settings/ # App configuration
└── util/ # Helper classes

## 👨‍💻 Author

**Anthony Kamau**
BBIT Third year student — Strathmore University
Mobile Application Development Project

---

## 📄 License

This project is open source and available under the MIT License.
