package com.anthonykamau.budgetapp.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.anthonykamau.budgetapp.data.entity.Expense
import com.anthonykamau.budgetapp.util.CurrencyUtils
import com.anthonykamau.budgetapp.util.DateUtils

class RecentExpenseAdapter : ListAdapter<Expense, RecentExpenseAdapter.ExpenseViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        // create a simple row view programmatically
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_2, parent, false)
        return ExpenseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ExpenseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val text1: TextView = itemView.findViewById(android.R.id.text1)
        private val text2: TextView = itemView.findViewById(android.R.id.text2)

        fun bind(expense: Expense) {
            text1.text = "${expense.categoryEmoji} ${expense.categoryName}"
            text1.setTextColor(0xFFFFFFFF.toInt())
            text2.text = "- ${CurrencyUtils.formatAmount(expense.amount)}  •  ${DateUtils.formatDateShort(expense.date)}"
            text2.setTextColor(0xFFF87171.toInt()) // red_soft
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Expense>() {
        override fun areItemsTheSame(oldItem: Expense, newItem: Expense) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Expense, newItem: Expense) = oldItem == newItem
    }
}