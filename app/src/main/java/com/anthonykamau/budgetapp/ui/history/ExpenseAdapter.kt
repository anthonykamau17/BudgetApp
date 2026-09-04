package com.anthonykamau.budgetapp.ui.history

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

class ExpenseAdapter(
    private val onLongClick: (Expense) -> Unit
) : ListAdapter<Expense, ExpenseAdapter.ExpenseViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_expense, parent, false)
        return ExpenseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ExpenseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvEmoji = itemView.findViewById<TextView>(R.id.tv_category_emoji)
        private val tvName = itemView.findViewById<TextView>(R.id.tv_expense_name)
        private val tvNote = itemView.findViewById<TextView>(R.id.tv_expense_note)
        private val tvDate = itemView.findViewById<TextView>(R.id.tv_expense_date)
        private val tvAmount = itemView.findViewById<TextView>(R.id.tv_expense_amount)

        fun bind(expense: Expense) {
            tvEmoji.text = expense.categoryEmoji
            tvName.text = expense.categoryName

            // show note if it exists
            if (!expense.note.isNullOrEmpty()) {
                tvNote.text = expense.note
                tvNote.visibility = View.VISIBLE
            } else {
                tvNote.visibility = View.GONE
            }

            tvDate.text = DateUtils.getDateGroupLabel(expense.date)
            tvAmount.text = "- ${CurrencyUtils.formatAmountShort(expense.amount)}"

            // long press to delete
            itemView.setOnLongClickListener {
                onLongClick(expense)
                true
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Expense>() {
        override fun areItemsTheSame(oldItem: Expense, newItem: Expense) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Expense, newItem: Expense) = oldItem == newItem
    }
}