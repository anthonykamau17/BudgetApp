package com.anthonykamau.budgetapp.ui.analytics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.anthonykamau.budgetapp.R
import com.anthonykamau.budgetapp.data.entity.Expense
import com.anthonykamau.budgetapp.util.CurrencyUtils
import com.anthonykamau.budgetapp.util.DateUtils

class AnalyticsFragment : Fragment() {

    private val viewModel: AnalyticsViewModel by viewModels()
    private var currentTab = "week"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_analytics, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tabWeek = view.findViewById<TextView>(R.id.tab_week)
        val tabMonth = view.findViewById<TextView>(R.id.tab_month)
        val tabSemester = view.findViewById<TextView>(R.id.tab_semester)

        // tab click listeners
        tabWeek.setOnClickListener {
            currentTab = "week"
            updateTabStyles(view, "week")
            viewModel.weekExpenses.value?.let { updateStats(view, it) }
        }

        tabMonth.setOnClickListener {
            currentTab = "month"
            updateTabStyles(view, "month")
            viewModel.monthExpenses.value?.let { updateStats(view, it) }
        }

        tabSemester.setOnClickListener {
            currentTab = "semester"
            updateTabStyles(view, "semester")
            viewModel.semesterExpenses.value?.let { updateStats(view, it) }
        }

        // observe week expenses (default tab)
        viewModel.weekExpenses.observe(viewLifecycleOwner) { expenses ->
            if (currentTab == "week") updateStats(view, expenses)
        }

        viewModel.monthExpenses.observe(viewLifecycleOwner) { expenses ->
            if (currentTab == "month") updateStats(view, expenses)
        }

        viewModel.semesterExpenses.observe(viewLifecycleOwner) { expenses ->
            if (currentTab == "semester") updateStats(view, expenses)
        }

        // observe category totals
        viewModel.categoryTotals.observe(viewLifecycleOwner) { totals ->
            buildCategoryBreakdown(view, totals.map { it.categoryName to it.total })
        }
    }

    private fun updateStats(view: View, expenses: List<Expense>) {
        val tvTotal = view.findViewById<TextView>(R.id.tv_total_spent)
        val tvCount = view.findViewById<TextView>(R.id.tv_transaction_count)
        val tvAvg = view.findViewById<TextView>(R.id.tv_avg_per_day)
        val tvInsight = view.findViewById<TextView>(R.id.tv_insight)
        val tvEmpty = view.findViewById<TextView>(R.id.tv_empty_analytics)
        val llBreakdown = view.findViewById<LinearLayout>(R.id.ll_category_breakdown)

        val total = expenses.sumOf { it.amount }
        val count = expenses.size
        val daysElapsed = DateUtils.getDaysElapsedInMonth().coerceAtLeast(1)
        val avg = total / daysElapsed

        tvTotal.text = CurrencyUtils.formatAmountShort(total)
        tvCount.text = count.toString()
        tvAvg.text = CurrencyUtils.formatAmountShort(avg)

        // insight message
        tvInsight.text = if (expenses.isEmpty()) {
            "No expenses recorded yet. Start tracking!"
        } else {
            val topCategory = expenses.groupBy { it.categoryName }
                .maxByOrNull { it.value.sumOf { e -> e.amount } }
            "Your top spend is ${topCategory?.key ?: "unknown"}. " +
                    "You've made $count transactions totalling ${CurrencyUtils.formatAmountShort(total)}."
        }

        // empty state
        tvEmpty.visibility = if (expenses.isEmpty()) View.VISIBLE else View.GONE
        llBreakdown.visibility = if (expenses.isEmpty()) View.GONE else View.VISIBLE
    }

    private fun updateTabStyles(view: View, activeTab: String) {
        val tabWeek = view.findViewById<TextView>(R.id.tab_week)
        val tabMonth = view.findViewById<TextView>(R.id.tab_month)
        val tabSemester = view.findViewById<TextView>(R.id.tab_semester)

        val activeColor = resources.getColor(R.color.orange_bright, null)
        val inactiveColor = resources.getColor(R.color.bg_card, null)
        val activeText = resources.getColor(R.color.bg_base, null)
        val inactiveText = resources.getColor(R.color.text_tertiary, null)

        tabWeek.setBackgroundColor(if (activeTab == "week") activeColor else inactiveColor)
        tabWeek.setTextColor(if (activeTab == "week") activeText else inactiveText)

        tabMonth.setBackgroundColor(if (activeTab == "month") activeColor else inactiveColor)
        tabMonth.setTextColor(if (activeTab == "month") activeText else inactiveText)

        tabSemester.setBackgroundColor(if (activeTab == "semester") activeColor else inactiveColor)
        tabSemester.setTextColor(if (activeTab == "semester") activeText else inactiveText)
    }

    private fun buildCategoryBreakdown(
        view: View,
        categoryTotals: List<Pair<String, Double>>
    ) {
        val container = view.findViewById<LinearLayout>(R.id.ll_category_breakdown)
        container.removeAllViews()

        if (categoryTotals.isEmpty()) return

        val grandTotal = categoryTotals.sumOf { it.second }

        categoryTotals.forEach { (name, total) ->
            val percentage = if (grandTotal > 0) ((total / grandTotal) * 100).toInt() else 0
            val isTop = categoryTotals.first().first == name

            // row container
            val row = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(0, 0, 0, 12)
            }

            // name + amount row
            val topRow = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.HORIZONTAL
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            }

            val tvName = TextView(requireContext()).apply {
                text = name
                textSize = 12f
                setTextColor(resources.getColor(R.color.text_primary, null))
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            }

            val tvAmount = TextView(requireContext()).apply {
                text = "${CurrencyUtils.formatAmountShort(total)}  ($percentage%)"
                textSize = 12f
                setTextColor(
                    if (isTop) resources.getColor(R.color.orange_bright, null)
                    else resources.getColor(R.color.text_secondary, null)
                )
            }

            topRow.addView(tvName)
            topRow.addView(tvAmount)

            // progress bar
            val progressBar = ProgressBar(
                requireContext(), null,
                android.R.attr.progressBarStyleHorizontal
            ).apply {
                max = 100
                progress = percentage
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 10
                ).apply { topMargin = 6 }
                progressTintList = android.content.res.ColorStateList.valueOf(
                    if (isTop) resources.getColor(R.color.orange_bright, null)
                    else resources.getColor(R.color.blue_accent, null)
                )
                progressBackgroundTintList = android.content.res.ColorStateList.valueOf(
                    resources.getColor(R.color.bg_deep, null)
                )
            }

            row.addView(topRow)
            row.addView(progressBar)
            container.addView(row)
        }
    }
}