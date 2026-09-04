package com.anthonykamau.budgetapp.util

import java.text.NumberFormat
import java.util.*

object CurrencyUtils {

    // formats a number to "KES 4,820" for display
    fun formatAmount(amount: Double, currency: String = "KES"): String {
        val formatter = NumberFormat.getNumberInstance(Locale.getDefault())
        formatter.maximumFractionDigits = 2
        formatter.minimumFractionDigits = 0
        return "$currency ${formatter.format(amount)}"
    }

    // formats without currency prefix — just "4,820" for compact display
    fun formatAmountShort(amount: Double): String {
        val formatter = NumberFormat.getNumberInstance(Locale.getDefault())
        formatter.maximumFractionDigits = 2
        formatter.minimumFractionDigits = 0
        return formatter.format(amount)
    }

    // parses a string like "240" or "1,200" back to a Double safely
    fun parseAmount(input: String): Double {
        return input.replace(",", "").toDoubleOrNull() ?: 0.0
    }
}