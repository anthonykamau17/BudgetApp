package com.anthonykamau.budgetapp.util

import com.anthonykamau.budgetapp.data.entity.Expense
import kotlin.math.roundToInt

object FinancialSensei {

    fun generateInsights(
        userName: String,
        monthlyBudget: Double,
        totalSpent: Double,
        daysLeft: Int,
        daysElapsed: Int,
        expenses: List<Expense>
    ): List<String> {
        val insights = mutableListOf<String>()
        val remaining = monthlyBudget - totalSpent
        val dailyAverage = if (daysElapsed > 0) totalSpent / daysElapsed else 0.0
        val projectedSpend = dailyAverage * (daysElapsed + daysLeft)

        // 1. Burn Rate Analysis (The "Broke-o-meter")
        if (monthlyBudget <= 0) {
            insights.add("Habari, $userName! 🤖 Set a monthly budget in Settings so I can actually judge—I mean, help you.")
            return insights
        }

        insights.add("Habari, $userName! 🏯 I've crunched the numbers. Here is your financial 'reality check'.")

        if (projectedSpend > monthlyBudget) {
            val deficit = projectedSpend - monthlyBudget
            insights.add("⚠️ BURN RATE ALERT: At this rate, you'll be over budget by KES ${deficit.roundToInt()}. Your wallet is on life support! 🚨")
        } else {
            insights.add("🎉 SAFE ZONE: You are actually being responsible. Your projected spend is within the limit. Don't ruin it now!")
        }

        // 2. Category Roasts (Kenyan Campus Edition)
        if (expenses.isNotEmpty()) {
            val categoryTotals = expenses.groupBy { it.categoryName }
                .mapValues { (_, list) -> list.sumOf { it.amount } }
            
            val topCategory = categoryTotals.maxByOrNull { it.value }
            
            topCategory?.let { (name, total) ->
                val percentage = ((total / totalSpent) * 100).roundToInt()
                
                val roast = when (name.lowercase()) {
                    "food" -> "🍔 $percentage% on Food? You're eating like a billionaire on a student budget. Hostel cooking isn't that bad!"
                    "coffee" -> "☕ $percentage% on Coffee? You're paying for the 'vibe', not the caffeine. Switch to instant mix and save those coins!"
                    "transport", "fare" -> "🚌 Transport is $percentage% of your spend. Are you taking Ubers to the next block? Consider walking for those 'steps' and savings!"
                    "fun" -> "🎮 $percentage% on 'Fun'? Sherehe haitaki hasira, but your bank account is definitely angry right now! 📉"
                    else -> "💸 $name is your biggest drain ($percentage%). Keep an eye on it before it swallows your whole allowance."
                }
                insights.add(roast)
            }
        }

        // 3. Tactical Survival Advice
        val dailyAllowed = if (daysLeft > 0) remaining / daysLeft else 0.0
        
        when {
            remaining <= 0 -> {
                insights.add("💀 MISSION FAILED: You are officially out of money. It's 'strong tea and prayers' for the rest of the month.")
            }
            dailyAllowed < 100 -> {
                insights.add("🚨 CRITICAL: You only have KES ${dailyAllowed.roundToInt()} per day left. Avoid all shops, malls, and friends who like spending!")
            }
            dailyAllowed < 300 -> {
                insights.add("💡 SMART TIP: Limiting yourself to KES ${dailyAllowed.roundToInt()} per day will keep you alive until next month.")
            }
            else -> {
                insights.add("💰 OPPORTUNITY: You have KES ${dailyAllowed.roundToInt()} daily left. If you spend less, that's extra KES for your savings goals! 🚀")
            }
        }

        return insights
    }
}
