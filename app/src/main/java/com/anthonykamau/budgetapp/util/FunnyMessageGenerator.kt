package com.anthonykamau.budgetapp.util

import com.anthonykamau.budgetapp.util.BudgetStatus

object FunnyMessageGenerator {

    // general spending messages — shown on Dashboard normally
    private val normalMessages = listOf(
        "You're doing okay. Don't ruin it.",
        "Money still exists in your account. Wild.",
        "Budget looking stable. Don't celebrate yet.",
        "You haven't broken the budget. Yet.",
        "Financially, you're surviving. Thriving is next week."
    )

    // shown when food is the top spending category
    private val foodMessages = listOf(
        "Food took the most of your budget. Your stomach is not your friend.",
        "68% on food. You're basically funding a restaurant.",
        "Your top expense is food. Have you considered cooking?",
        "Food spending is high. Your wallet cries every meal.",
        "Food: 1, Budget: 0. Classic."
    )

    // shown when transport is the top category
    private val transportMessages = listOf(
        "You've spent a lot on transport. The matatu is getting rich.",
        "Transport is your top expense. Have you considered walking?",
        "Half your money went to getting places. Worth it?"
    )

    // shown at 80% budget warning
    private val warningMessages = listOf(
        "You've used 80% of your budget. Slow. Down.",
        "80% gone. The remaining 20% is looking scared.",
        "Almost out of budget. Time to eat from home.",
        "Warning: your wallet is nearly empty. Be responsible.",
        "80% used. That remaining KES is carrying all the weight."
    )

    // shown at 100% budget exceeded
    private val exceededMessages = listOf(
        "Budget exceeded. Time to eat air.",
        "You've spent it all. Congratulations, I guess.",
        "Budget: gone. Dignity: intact (barely).",
        "No budget left. The month isn't over. Good luck.",
        "You broke the budget. Your ancestors are disappointed."
    )

    // prediction messages — shown below the balance card
    fun getPredictionMessage(daysLeft: Int, daysMoneyWillLast: Int): String {
        return when {
            daysMoneyWillLast >= daysLeft ->
                "At this pace, you'll survive the month. Barely."
            daysMoneyWillLast <= 0 ->
                "Money ran out. The month hasn't. That's a problem."
            else ->
                "At this pace, money runs out in $daysMoneyWillLast days. The month ends in $daysLeft."
        }
    }

    // main function — call this from DashboardViewModel
    fun getMessage(
        budgetStatus: BudgetStatus,
        topCategory: String = ""
    ): String {
        return when (budgetStatus) {
            BudgetStatus.EXCEEDED -> exceededMessages.random()
            BudgetStatus.WARNING  -> warningMessages.random()
            BudgetStatus.NORMAL   -> {
                when (topCategory.lowercase()) {
                    "food"      -> foodMessages.random()
                    "transport" -> transportMessages.random()
                    else        -> normalMessages.random()
                }
            }
        }
    }
}