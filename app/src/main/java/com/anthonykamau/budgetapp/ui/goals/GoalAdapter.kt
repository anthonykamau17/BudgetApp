package com.anthonykamau.budgetapp.ui.goals

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.anthonykamau.budgetapp.R
import com.anthonykamau.budgetapp.data.entity.SavingsGoal
import com.anthonykamau.budgetapp.util.CurrencyUtils

class GoalAdapter(
    private val onLongClick: (SavingsGoal) -> Unit,
    private val onTap: (SavingsGoal) -> Unit
) : ListAdapter<SavingsGoal, GoalAdapter.GoalViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_goal, parent, false)
        return GoalViewHolder(view)
    }

    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class GoalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvEmoji = itemView.findViewById<TextView>(R.id.tv_goal_emoji)
        private val tvName = itemView.findViewById<TextView>(R.id.tv_goal_name)
        private val tvSub = itemView.findViewById<TextView>(R.id.tv_goal_sub)
        private val tvPct = itemView.findViewById<TextView>(R.id.tv_goal_percentage)
        private val progress = itemView.findViewById<ProgressBar>(R.id.progress_goal)
        private val tvSaved = itemView.findViewById<TextView>(R.id.tv_goal_saved)
        private val tvRemaining = itemView.findViewById<TextView>(R.id.tv_goal_remaining)

        fun bind(goal: SavingsGoal) {
            itemView.setOnClickListener { onTap(goal) }
            tvEmoji.text = goal.emoji
            tvName.text = goal.name
            tvSub.text = "Target: ${CurrencyUtils.formatAmountShort(goal.targetAmount)}"

            val pct = if (goal.targetAmount > 0)
                ((goal.savedAmount / goal.targetAmount) * 100).toInt().coerceIn(0, 100)
            else 0

            tvPct.text = "$pct%"
            progress.progress = pct

            tvSaved.text = "${CurrencyUtils.formatAmountShort(goal.savedAmount)} saved"
            val toGo = (goal.targetAmount - goal.savedAmount).coerceAtLeast(0.0)
            tvRemaining.text = "${CurrencyUtils.formatAmountShort(toGo)} to go"

            itemView.setOnLongClickListener {
                onLongClick(goal)
                true
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<SavingsGoal>() {
        override fun areItemsTheSame(oldItem: SavingsGoal, newItem: SavingsGoal) =
            oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: SavingsGoal, newItem: SavingsGoal) =
            oldItem == newItem
    }
}