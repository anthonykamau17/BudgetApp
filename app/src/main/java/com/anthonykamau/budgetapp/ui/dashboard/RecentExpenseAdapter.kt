package com.anthonykamau.budgetapp.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.anthonykamau.budgetapp.R
import com.anthonykamau.budgetapp.data.entity.Expense
import com.anthonykamau.budgetapp.util.CurrencyUtils
import com.anthonykamau.budgetapp.util.DateUtils

class RecentExpenseAdapter : ListAdapter<Expense, RecentExpenseAdapter.ExpenseViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        // Inflate your premium custom layout instead of simple_list_item_2
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_expense, parent, false)
        return ExpenseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ExpenseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvEmoji: TextView = itemView.findViewById(R.id.tv_category_emoji)
        private val tvName: TextView = itemView.findViewById(R.id.tv_expense_name)
        private val tvDate: TextView = itemView.findViewById(R.id.tv_expense_date)
        private val tvAmount: TextView = itemView.findViewById(R.id.tv_expense_amount)
        private val tvNote: TextView = itemView.findViewById(R.id.tv_expense_note)

        fun bind(expense: Expense) {
            tvEmoji.text = expense.categoryEmoji
            tvName.text = expense.categoryName
            tvDate.text = DateUtils.formatDateShort(expense.date)
            tvAmount.text = "- ${CurrencyUtils.formatAmount(expense.amount)}"

            if (!expense.note.isNullOrBlank()) {
                tvNote.text = expense.note
                tvNote.visibility = View.VISIBLE
            } else {
                tvNote.visibility = View.GONE
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Expense>() {
        override fun areItemsTheSame(oldItem: Expense, newItem: Expense) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Expense, newItem: Expense) = oldItem == newItem
    }
}
