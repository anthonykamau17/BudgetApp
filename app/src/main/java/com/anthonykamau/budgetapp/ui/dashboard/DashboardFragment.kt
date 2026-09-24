package com.anthonykamau.budgetapp.ui.dashboard

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anthonykamau.budgetapp.R
import com.anthonykamau.budgetapp.data.entity.Expense
import com.anthonykamau.budgetapp.data.entity.CategoryBudget
import com.anthonykamau.budgetapp.util.CurrencyUtils
import com.anthonykamau.budgetapp.util.SurvivalCalculator

class DashboardFragment : Fragment() {

    private val viewModel: DashboardViewModel by viewModels()
    private lateinit var adapter: RecentExpenseAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Setup RecyclerView
        adapter = RecentExpenseAdapter()
        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_recent_expenses)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        // 2. Greeting
        view.findViewById<TextView>(R.id.tv_greeting).text = getGreeting()

        // 3. Observers with Null Safety
        viewModel.settings.observe(viewLifecycleOwner) { settings ->
            settings?.let {
                view.findViewById<TextView>(R.id.tv_user_name).text = 
                    if (it.userName.isNotEmpty()) "${it.userName} 👋" else "👋"
                
                view.findViewById<TextView>(R.id.tv_budget_total).text = 
                    "Budget: ${CurrencyUtils.formatAmount(it.monthlyBudget, it.currency)}"
            }
        }

        viewModel.remaining.observe(viewLifecycleOwner) { remaining ->
            if (remaining == null) return@observe
            val currency = viewModel.settings.value?.currency ?: "KES"
            val tvRemaining = view.findViewById<TextView>(R.id.tv_remaining)
            animateBalance(tvRemaining, remaining, currency)

            // Calculate Daily Left safely
            val daily = if (viewModel.daysLeftInMonth > 0) remaining / viewModel.daysLeftInMonth else 0.0
            view.findViewById<TextView>(R.id.tv_daily_left).text = CurrencyUtils.formatAmountShort(daily)
        }

        viewModel.totalSpent.observe(viewLifecycleOwner) { spent ->
            view.findViewById<TextView>(R.id.tv_spent).text = CurrencyUtils.formatAmountShort(spent ?: 0.0)
        }

        viewModel.totalSaved.observe(viewLifecycleOwner) { saved ->
            view.findViewById<TextView>(R.id.tv_saved).text = CurrencyUtils.formatAmountShort(saved ?: 0.0)
        }

        view.findViewById<TextView>(R.id.tv_days_left).text = viewModel.daysLeftInMonth.toString()

        viewModel.percentageUsed.observe(viewLifecycleOwner) { pct ->
            val safePct = pct ?: 0.0
            val progress = safePct.toInt().coerceIn(0, 100)
            view.findViewById<ProgressBar>(R.id.progress_budget).progress = progress
            view.findViewById<TextView>(R.id.tv_percent_remaining).text = "${(100 - progress)}% remaining"
        }

        viewModel.survivalForecast.observe(viewLifecycleOwner) { forecast ->
            forecast?.let {
                view.findViewById<TextView>(R.id.tv_forecast_message).text = it.message
                view.findViewById<TextView>(R.id.tv_forecast_emoji).text = when(it.status) {
                    SurvivalCalculator.SurvivalStatus.SAFE -> "✅"
                    SurvivalCalculator.SurvivalStatus.WARNING -> "⚠️"
                    SurvivalCalculator.SurvivalStatus.CRITICAL -> "🚨"
                    SurvivalCalculator.SurvivalStatus.BROKE -> "💀"
                }
            }
        }

        viewModel.funnyMessage.observe(viewLifecycleOwner) { message ->
            view.findViewById<TextView>(R.id.tv_funny_message).text = message ?: "Calculating..."
        }

        viewModel.recentExpenses.observe(viewLifecycleOwner) { expenses ->
            expenses?.let {
                adapter.submitList(it)
                view.findViewById<TextView>(R.id.tv_empty_recent).visibility = 
                    if (it.isEmpty()) View.VISIBLE else View.GONE
            }
        }

        // 4. Category Budget List
        val categoryBudgetAdapter = CategoryBudgetAdapter { }
        view.findViewById<RecyclerView>(R.id.rv_category_budgets).apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = categoryBudgetAdapter
        }

        viewModel.categoryBudgets.observe(viewLifecycleOwner) { budgets ->
            updateCategoryList(budgets, viewModel.allMonthExpenses.value, categoryBudgetAdapter)
        }
        viewModel.allMonthExpenses.observe(viewLifecycleOwner) { expenses ->
            updateCategoryList(viewModel.categoryBudgets.value, expenses, categoryBudgetAdapter)
        }

        // 5. Click Listeners
        view.findViewById<View>(R.id.btn_quick_food).setOnClickListener { navigateToAdd("Food") }
        view.findViewById<View>(R.id.btn_quick_fare).setOnClickListener { navigateToAdd("Transport") }
        view.findViewById<View>(R.id.btn_quick_books).setOnClickListener { navigateToAdd("Books") }
        view.findViewById<View>(R.id.btn_quick_coffee).setOnClickListener { navigateToAdd("Coffee") }
        view.findViewById<View>(R.id.btn_ai_coach).setOnClickListener { findNavController().navigate(R.id.aiCoachFragment) }
        view.findViewById<TextView>(R.id.tv_see_all).setOnClickListener { findNavController().navigate(R.id.historyFragment) }
    }

    private fun updateCategoryList(budgets: List<CategoryBudget>?, expenses: List<Expense>?, adapter: CategoryBudgetAdapter) {
        if (budgets == null || expenses == null) return
        val display = budgets.map { budget ->
            val spent = expenses.filter { it.categoryName == budget.categoryName }.sumOf { it.amount }
            CategoryBudgetDisplay(budget.categoryName, budget.emoji, budget.budgetLimit, spent)
        }
        adapter.submitList(display)
    }

    private fun navigateToAdd(category: String) {
        val bundle = Bundle().apply { putString("categoryName", category) }
        findNavController().navigate(R.id.addExpenseFragment, bundle)
    }

    private fun animateBalance(textView: TextView, targetAmount: Double, currency: String) {
        ValueAnimator.ofFloat(0f, targetAmount.toFloat()).apply {
            duration = 1000
            interpolator = android.view.animation.DecelerateInterpolator()
            addUpdateListener { textView.text = CurrencyUtils.formatAmount((it.animatedValue as Float).toDouble(), currency) }
            start()
        }
    }

    private fun getGreeting() = when (java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)) {
        in 0..11 -> "Good morning,"
        in 12..16 -> "Good afternoon,"
        else -> "Good evening,"
    }
}
