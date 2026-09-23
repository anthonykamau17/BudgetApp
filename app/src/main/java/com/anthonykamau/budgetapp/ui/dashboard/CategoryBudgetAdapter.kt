package com.anthonykamau.budgetapp.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.anthonykamau.budgetapp.R
import com.anthonykamau.budgetapp.util.CurrencyUtils

data class CategoryBudgetDisplay(
    val categoryName: String,
    val emoji: String,
    val budgetLimit: Double,
    val amountSpent: Double
)

class CategoryBudgetAdapter(
    private val onTap: (CategoryBudgetDisplay) -> Unit
) : ListAdapter<CategoryBudgetDisplay, CategoryBudgetAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category_budget, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvEmoji = itemView.findViewById<TextView>(R.id.tv_cat_emoji)
        private val tvName = itemView.findViewById<TextView>(R.id.tv_cat_name)
        private val tvLimit = itemView.findViewById<TextView>(R.id.tv_cat_limit)
        private val progress = itemView.findViewById<ProgressBar>(R.id.progress_cat)
        private val tvSpent = itemView.findViewById<TextView>(R.id.tv_cat_spent)
        private val tvRemaining = itemView.findViewById<TextView>(R.id.tv_cat_remaining)

        fun bind(item: CategoryBudgetDisplay) {
            tvEmoji.text = item.emoji
            tvName.text = item.categoryName
            tvLimit.text = CurrencyUtils.formatAmountShort(item.budgetLimit)

            val pct = if (item.budgetLimit > 0)
                ((item.amountSpent / item.budgetLimit) * 100).toInt().coerceIn(0, 100)
            else 0

            progress.progress = pct

            // change colour based on how much is used
            val tintColor = when {
                pct >= 100 -> 0xFFF87171.toInt() // red
                pct >= 80  -> 0xFFFAC775.toInt() // orange dim
                else       -> 0xFFF59E0B.toInt() // orange bright
            }
            progress.progressTintList = android.content.res.ColorStateList.valueOf(tintColor)

            tvSpent.text = "${CurrencyUtils.formatAmountShort(item.amountSpent)} spent"
            val remaining = (item.budgetLimit - item.amountSpent).coerceAtLeast(0.0)
            tvRemaining.text = "${CurrencyUtils.formatAmountShort(remaining)} left"

            itemView.setOnClickListener { onTap(item) }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<CategoryBudgetDisplay>() {
        override fun areItemsTheSame(a: CategoryBudgetDisplay, b: CategoryBudgetDisplay) =
            a.categoryName == b.categoryName
        override fun areContentsTheSame(a: CategoryBudgetDisplay, b: CategoryBudgetDisplay) =
            a == b
    }
}