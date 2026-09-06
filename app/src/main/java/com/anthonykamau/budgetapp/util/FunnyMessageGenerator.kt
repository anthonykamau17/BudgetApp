package com.anthonykamau.budgetapp.util

import com.anthonykamau.budgetapp.util.BudgetStatus

object FunnyMessageGenerator {

    private val normalMessages = listOf(
        "You're doing okay. Don't ruin it.",
        "Money still exists in your account. A miracle.",
        "Budget looking stable. Don't celebrate yet.",
        "You haven't broken the budget. Yet.",
        "Financially, you're surviving. Thriving is next month.",
        "Your wallet is still breathing. Barely.",
        "HELB came through and you're still broke. Impressive.",
        "End month is coming. Prepare yourself.",
        "You're spending like you have a salary. You don't."
    )

    private val foodMessages = listOf(
        "Food took the most of your budget. Your stomach is not your friend.",
        "You're basically running a one-person restaurant.",
        "Kenchic and Naivas are thriving because of you.",
        "Food: 1. Budget: 0. Classic student story.",
        "Have you considered cooking? Just asking.",
        "Your top expense is food. The uji at home was free.",
        "You spent more on food than anything else. Respect."
    )

    private val transportMessages = listOf(
        "You've spent a lot on transport. The matatu is getting rich.",
        "Transport is your top expense. Have you considered walking?",
        "Half your money went to fare. The conductor says thank you.",
        "Matatu fare adding up. Maybe campus hostels aren't that bad.",
        "You're funding Nairobi's transport sector single-handedly."
    )

    private val warningMessages = listOf(
        "You've used 80% of your budget. Slow. Down.",
        "80% gone. The remaining 20% is looking scared.",
        "Almost out of budget. Time to eat from the house.",
        "Warning: wallet nearly empty. Call your parents.",
        "80% used. That remaining KES is carrying all the weight.",
        "Danger zone. Hide your M-Pesa from yourself.",
        "At this rate, you'll be surviving on black tea by month end."
    )

    private val exceededMessages = listOf(
        "Budget exceeded. Time to eat air.",
        "You've spent it all. Congratulations, I guess.",
        "Budget: gone. Dignity: intact (barely).",
        "No budget left. The month isn't over. Good luck.",
        "You broke the budget. Your ancestors are disappointed.",
        "M-Pesa balance: painful. Budget: non-existent.",
        "End month achieved early. A new personal record.",
        "Time to text 'nisaidie kidogo' to someone. Anyone."
    )

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