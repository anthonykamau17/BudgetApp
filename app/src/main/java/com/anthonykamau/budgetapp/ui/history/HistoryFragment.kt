package com.anthonykamau.budgetapp.ui.history

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anthonykamau.budgetapp.R
import com.anthonykamau.budgetapp.data.entity.Expense
import com.anthonykamau.budgetapp.util.CurrencyUtils

class HistoryFragment : Fragment() {

    private val viewModel: HistoryViewModel by viewModels()
    private lateinit var adapter: ExpenseAdapter
    private var allExpenses = listOf<Expense>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_history)
        val etSearch = view.findViewById<EditText>(R.id.et_search)
        val tvEmpty = view.findViewById<TextView>(R.id.tv_empty_history)
        val tvTotal = view.findViewById<TextView>(R.id.tv_total_label)

        // set up adapter with long click delete
        adapter = ExpenseAdapter { expense ->
            showDeleteDialog(expense)
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        // observe all expenses
        viewModel.allExpenses.observe(viewLifecycleOwner) { expenses ->
            allExpenses = expenses
            updateList(expenses)

            // update total
            val total = expenses.sumOf { it.amount }
            tvTotal.text = "Total: ${CurrencyUtils.formatAmountShort(total)}"

            // empty state
            tvEmpty.visibility = if (expenses.isEmpty()) View.VISIBLE else View.GONE
            recyclerView.visibility = if (expenses.isEmpty()) View.GONE else View.VISIBLE
        }

        // search functionality
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().lowercase()
                val filtered = if (query.isEmpty()) {
                    allExpenses
                } else {
                    allExpenses.filter {
                        it.categoryName.lowercase().contains(query) ||
                                it.note?.lowercase()?.contains(query) == true
                    }
                }
                updateList(filtered)
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun updateList(expenses: List<Expense>) {
        adapter.submitList(expenses.toList())
    }

    private fun showDeleteDialog(expense: Expense) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete expense?")
            .setMessage("Delete ${expense.categoryEmoji} ${expense.categoryName} — ${expense.amount}?")
            .setPositiveButton("Delete") { _, _ ->
                viewModel.deleteExpense(expense)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}