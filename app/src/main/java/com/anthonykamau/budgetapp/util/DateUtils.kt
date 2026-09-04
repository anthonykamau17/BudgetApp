package com.anthonykamau.budgetapp.util

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    // returns the Unix timestamp for the start of the current month
    fun getStartOfCurrentMonth(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

    // how many days are left in the current month
    fun getDaysLeftInMonth(): Int {
        val calendar = Calendar.getInstance()
        val totalDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val today = calendar.get(Calendar.DAY_OF_MONTH)
        return totalDays - today
    }

    // how many days have passed since the start of the month
    fun getDaysElapsedInMonth(): Int {
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.DAY_OF_MONTH)
    }

    // formats a timestamp to "Nov 12" for History screen
    fun formatDateShort(timestamp: Long): String {
        val sdf = SimpleDateFormat("MMM d", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    // formats a timestamp to "12 November 2025" for full display
    fun formatDateFull(timestamp: Long): String {
        val sdf = SimpleDateFormat("d MMMM yyyy", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    // formats a timestamp to "9:41 AM" for time display
    fun formatTime(timestamp: Long): String {
        val sdf = SimpleDateFormat("h:mm a", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    // returns a human-readable group label for History list headers
    fun getDateGroupLabel(timestamp: Long): String {
        val today = Calendar.getInstance()
        val expenseDay = Calendar.getInstance().apply { timeInMillis = timestamp }

        return when {
            isSameDay(today, expenseDay) -> "Today"
            isYesterday(today, expenseDay) -> "Yesterday"
            else -> formatDateShort(timestamp)
        }
    }

    // checks if a target date is X days ago from today — used for goal deadlines
    fun getDaysUntil(targetTimestamp: Long): Int {
        val now = System.currentTimeMillis()
        val diff = targetTimestamp - now
        return (diff / (1000 * 60 * 60 * 24)).toInt()
    }

    private fun isSameDay(cal1: Calendar, cal2: Calendar): Boolean {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }

    private fun isYesterday(today: Calendar, other: Calendar): Boolean {
        val yesterday = Calendar.getInstance().apply {
            timeInMillis = today.timeInMillis
            add(Calendar.DAY_OF_YEAR, -1)
        }
        return isSameDay(yesterday, other)
    }
}