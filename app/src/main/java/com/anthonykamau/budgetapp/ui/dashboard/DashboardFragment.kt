package com.anthonykamau.budgetapp.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anthonykamau.budgetapp.R
import com.anthonykamau.budgetapp.util.CurrencyUtils
import com.anthonykamau.budgetapp.util.DateUtils
import com.anthonykamau.budgetapp.util.SurvivalCalculator
import android.animation.ValueAnimator

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

        // set up RecyclerView
        adapter = RecentExpenseAdapter()
        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_recent_expenses)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        // greeting
        val tvGreeting = view.findViewById<TextView>(R.id.tv_greeting)
        tvGreeting.text = getGreeting()

        // observe settings for user name and budget
        viewModel.settings.observe(viewLifecycleOwner) { settings ->
            if (settings != null) {
                val tvUserName = view.findViewById<TextView>(R.id.tv_user_name)
                tvUserName.text = if (settings.userName.isNotEmpty())
                    "${settings.userName} 👋" else "👋"

                val tvBudgetTotal = view.findViewById<TextView>(R.id.tv_budget_total)
                tvBudgetTotal.text = "Budget: ${CurrencyUtils.formatAmount(settings.monthlyBudget, settings.currency)}"
            }
        }

        // observe remaining balance
        viewModel.remaining.observe(viewLifecycleOwner) { remaining ->
            val currency = viewModel.settings.value?.currency ?: "KES"
            val tvRemaining = view.findViewById<TextView>(R.id.tv_remaining)
            animateBalance(tvRemaining, remaining, currency)
        }

        // observe total spent
        viewModel.totalSpent.observe(viewLifecycleOwner) { spent ->
            view.findViewById<TextView>(R.id.tv_spent).text =
                CurrencyUtils.formatAmountShort(spent)
        }

        // observe total saved
        viewModel.totalSaved.observe(viewLifecycleOwner) { saved ->
            view.findViewById<TextView>(R.id.tv_saved).text =
                CurrencyUtils.formatAmountShort(saved)
        }

        // days left
        view.findViewById<TextView>(R.id.tv_days_left).text =
            viewModel.daysLeftInMonth.toString()

        // observe daily budget left (mapped to "Daily left" card)
        viewModel.remaining.observe(viewLifecycleOwner) { rem ->
            val daily = if (viewModel.daysLeftInMonth > 0) (rem ?: 0.0) / viewModel.daysLeftInMonth else 0.0
            view.findViewById<TextView>(R.id.tv_daily_left).text =
                CurrencyUtils.formatAmountShort(daily)
        }

        // observe percentage used — update progress bar
        viewModel.percentageUsed.observe(viewLifecycleOwner) { pct ->
            val progress = pct.toInt().coerceIn(0, 100)
            view.findViewById<ProgressBar>(R.id.progress_budget).progress = progress
            view.findViewById<TextView>(R.id.tv_percent_remaining).text =
                "${(100 - progress)}% remaining"
        }

        // observe survival forecast
        val tvForecastEmoji = view.findViewById<TextView>(R.id.tv_forecast_emoji)
        val tvForecastMsg = view.findViewById<TextView>(R.id.tv_forecast_message)
        
        viewModel.survivalForecast.observe(viewLifecycleOwner) { forecast ->
            tvForecastMsg.text = forecast.message
            tvForecastEmoji.text = when(forecast.status) {
                SurvivalCalculator.SurvivalStatus.SAFE -> "✅"
                SurvivalCalculator.SurvivalStatus.WARNING -> "⚠️"
                SurvivalCalculator.SurvivalStatus.CRITICAL -> "🚨"
                SurvivalCalculator.SurvivalStatus.BROKE -> "💀"
            }
        }

        // observe funny message
        viewModel.funnyMessage.observe(viewLifecycleOwner) { message ->
            view.findViewById<TextView>(R.id.tv_funny_message).text = message
        }

        // observe recent expenses
        viewModel.recentExpenses.observe(viewLifecycleOwner) { expenses ->
            adapter.submitList(expenses)
            val emptyView = view.findViewById<TextView>(R.id.tv_empty_recent)
            emptyView.visibility = if (expenses.isEmpty()) View.VISIBLE else View.GONE
        }

        // category budget display
        val categoryBudgetAdapter = CategoryBudgetAdapter { }
        val rvCategoryBudgets = view.findViewById<RecyclerView>(R.id.rv_category_budgets)
        rvCategoryBudgets.layoutManager = LinearLayoutManager(requireContext())
        rvCategoryBudgets.adapter = categoryBudgetAdapter

        viewModel.categoryBudgets.observe(viewLifecycleOwner) { budgets ->
            viewModel.allMonthExpenses.observe(viewLifecycleOwner) { expenses ->
                val display = budgets.map { budget ->
                    val spent = expenses
                        .filter { it.categoryName == budget.categoryName }
                        .sumOf { it.amount }
                    CategoryBudgetDisplay(
                        categoryName = budget.categoryName,
                        emoji = budget.emoji,
                        budgetLimit = budget.budgetLimit,
                        amountSpent = spent
                    )
                }
                categoryBudgetAdapter.submitList(display)
            }
        }

        // quick add buttons
        view.findViewById<LinearLayout>(R.id.btn_quick_food).setOnClickListener {
            val bundle = android.os.Bundle().apply { putString("categoryName", "Food") }
            findNavController().navigate(R.id.addExpenseFragment, bundle)
        }
        view.findViewById<LinearLayout>(R.id.btn_quick_fare).setOnClickListener {
            val bundle = android.os.Bundle().apply { putString("categoryName", "Transport") }
            findNavController().navigate(R.id.addExpenseFragment, bundle)
        }
        view.findViewById<LinearLayout>(R.id.btn_quick_books).setOnClickListener {
            val bundle = android.os.Bundle().apply { putString("categoryName", "Books") }
            findNavController().navigate(R.id.addExpenseFragment, bundle)
        }
        view.findViewById<LinearLayout>(R.id.btn_quick_coffee).setOnClickListener {
            val bundle = android.os.Bundle().apply { putString("categoryName", "Coffee") }
            findNavController().navigate(R.id.addExpenseFragment, bundle)
        }

        // AI coach button
        view.findViewById<androidx.cardview.widget.CardView>(R.id.btn_ai_coach).setOnClickListener {
            findNavController().navigate(R.id.aiCoachFragment)
        }

        // see all
        view.findViewById<TextView>(R.id.tv_see_all).setOnClickListener {
            findNavController().navigate(R.id.historyFragment)
        }
    }

    private fun animateBalance(textView: TextView, targetAmount: Double, currency: String) {
        val animator = ValueAnimator.ofFloat(0f, targetAmount.toFloat())
        animator.duration = 1000
        animator.interpolator = android.view.animation.DecelerateInterpolator()
        animator.addUpdateListener { animation ->
            val animatedValue = animation.animatedValue as Float
            textView.text = CurrencyUtils.formatAmount(animatedValue.toDouble(), currency)
        }
        animator.start()
    }

    private fun getGreeting(): String {
        val hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
        return when {
            hour < 12 -> "Good morning,"
            hour < 17 -> "Good afternoon,"
            else      -> "Good evening,"
        }
    }
}
