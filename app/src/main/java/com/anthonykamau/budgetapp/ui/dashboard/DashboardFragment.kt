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
            view.findViewById<TextView>(R.id.tv_remaining).text =
                CurrencyUtils.formatAmount(remaining, currency)
        }

        // observe total spent
        viewModel.totalSpent.observe(viewLifecycleOwner) { spent ->
            val currency = viewModel.settings.value?.currency ?: "KES"
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

        // observe daily budget left
        viewModel.dailyBudgetLeft.observe(viewLifecycleOwner) { daily ->
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

        // quick add buttons — all navigate to Add Expense
        view.findViewById<LinearLayout>(R.id.btn_quick_food).setOnClickListener {
            findNavController().navigate(R.id.addExpenseFragment)
        }
        view.findViewById<LinearLayout>(R.id.btn_quick_fare).setOnClickListener {
            findNavController().navigate(R.id.addExpenseFragment)
        }
        view.findViewById<LinearLayout>(R.id.btn_quick_books).setOnClickListener {
            findNavController().navigate(R.id.addExpenseFragment)
        }
        view.findViewById<LinearLayout>(R.id.btn_quick_coffee).setOnClickListener {
            findNavController().navigate(R.id.addExpenseFragment)
        }

        // see all → navigate to history
        view.findViewById<TextView>(R.id.tv_see_all).setOnClickListener {
            findNavController().navigate(R.id.historyFragment)
        }
    }

    // returns greeting based on time of day
    private fun getGreeting(): String {
        val hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
        return when {
            hour < 12 -> "Good morning,"
            hour < 17 -> "Good afternoon,"
            else      -> "Good evening,"
        }
    }
}