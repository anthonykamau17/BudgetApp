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

        // 4. DYNAMIC INVESTING & SAVINGS ADVICE (V3.0 Smart Engine Addition)
        val dailyBurnPercentage = if (monthlyBudget > 0) (totalSpent / monthlyBudget) * 100 else 0.0
        
        when {
            // Surplus Zone: Low burn rate or plenty of funds left
            dailyBurnPercentage < 60 && remaining > 5000 -> {
                insights.add("🎯 SENSEI'S SAVINGS ADVICE: You have a healthy surplus! Don't leave extra cash idle in M-Pesa where a single 'sherehe' plan can wipe it out. Lock at least KES 1,500 into your custom Savings Goal immediately!")
                insights.add("📈 SENSEI'S INVESTING TIP: Since your burn rate is low, you are ready for a Money Market Fund (MMF). Look into platforms like CIC, Sanlam, or Britam. They accept entries as low as KES 100/500 and earn you daily compound interest instead of flat bank fees.")
            }
            // Mid-Tier Stable Zone: Moderate budget remaining
            dailyBurnPercentage >= 60 && remaining > 1500 -> {
                insights.add("🎯 SENSEI'S SAVINGS ADVICE: Implement the 10% rule. Every time an allowance, pocket money, or soft cash lands, save 10% *first* before opening this tracker to log your daily food or fare.")
                insights.add("📈 SENSEI'S INVESTING TIP: Focus on micro-investing. Rather than high-risk trading, look at fractional government bonds or treasury bills via custom banking apps, where small leftovers work for you securely.")
            }
            // Deficit / Emergency Warning Zone: Out of cash or near broke
            else -> {
                insights.add("🎯 SENSEI'S SAVINGS ADVICE: Stop thinking about long-term investing right now! Your immediate goal is building an Emergency Fund equivalent to 1 month of baseline campus survival expenses to completely avoid bad debts or emergency loans.")
                insights.add("📈 SENSEI'S INVESTING TIP: Your ultimate investment right now is defensive budgeting. Cut all silent micro-drains (like random automated subscriptions, extra luxury soda cycles, or delivery apps) to safeguard your remaining coins.")
            }
        }

        return insights
    }
}
