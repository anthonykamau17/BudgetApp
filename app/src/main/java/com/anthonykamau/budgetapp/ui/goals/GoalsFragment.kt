package com.anthonykamau.budgetapp.ui.goals

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anthonykamau.budgetapp.R
import com.anthonykamau.budgetapp.data.entity.SavingsGoal
import com.anthonykamau.budgetapp.util.CurrencyUtils

class GoalsFragment : Fragment() {

    private val viewModel: GoalsViewModel by viewModels()
    private lateinit var adapter: GoalAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_goals, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_goals)
        val tvEmpty = view.findViewById<TextView>(R.id.tv_empty_goals)
        val tvTotalSaved = view.findViewById<TextView>(R.id.tv_total_saved)
        val btnAddGoal = view.findViewById<View>(R.id.btn_add_goal)

        // set up adapter
        adapter = GoalAdapter(
            onLongClick = { goal -> showDeleteDialog(goal) },
            onTap = { goal -> showUpdateSavedDialog(goal) }
        )
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        // observe goals
        viewModel.allGoals.observe(viewLifecycleOwner) { goals ->
            adapter.submitList(goals)
            tvEmpty.visibility = if (goals.isEmpty()) View.VISIBLE else View.GONE
            recyclerView.visibility = if (goals.isEmpty()) View.GONE else View.VISIBLE
        }

        // observe total saved
        viewModel.totalSaved.observe(viewLifecycleOwner) { total ->
            tvTotalSaved.text = "Total saved: ${CurrencyUtils.formatAmountShort(total)}"
        }

        // add goal button
        btnAddGoal.setOnClickListener {
            showAddGoalDialog()
        }
    }

    private fun showAddGoalDialog() {
        val dialogView = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 32, 48, 16)
        }

        val etName = EditText(requireContext()).apply {
            hint = "Goal name (e.g. Holiday Fund)"
            textSize = 14f
        }
        val etEmoji = EditText(requireContext()).apply {
            hint = "Emoji (e.g. 🏖️)"
            textSize = 14f
        }
        val etTarget = EditText(requireContext()).apply {
            hint = "Target amount (e.g. 5000)"
            inputType = android.text.InputType.TYPE_CLASS_NUMBER or
                    android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
            textSize = 14f
        }
        val etSaved = EditText(requireContext()).apply {
            hint = "Already saved (e.g. 0)"
            inputType = android.text.InputType.TYPE_CLASS_NUMBER or
                    android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
            textSize = 14f
        }

        dialogView.addView(etName)
        dialogView.addView(etEmoji)
        dialogView.addView(etTarget)
        dialogView.addView(etSaved)

        AlertDialog.Builder(requireContext())
            .setTitle("New savings goal")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val name = etName.text.toString().trim()
                val emoji = etEmoji.text.toString().trim().ifEmpty { "🎯" }
                val target = etTarget.text.toString().toDoubleOrNull() ?: 0.0
                val saved = etSaved.text.toString().toDoubleOrNull() ?: 0.0

                if (name.isEmpty()) {
                    Toast.makeText(requireContext(), "Enter a goal name", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                if (target <= 0) {
                    Toast.makeText(requireContext(), "Enter a target amount", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                viewModel.insertGoal(
                    SavingsGoal(
                        name = name,
                        emoji = emoji,
                        targetAmount = target,
                        savedAmount = saved
                    )
                )
                Toast.makeText(requireContext(), "Goal added! 🎯", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showDeleteDialog(goal: SavingsGoal) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete goal?")
            .setMessage("Delete \"${goal.name}\"? This cannot be undone.")
            .setPositiveButton("Delete") { _, _ ->
                viewModel.deleteGoal(goal)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    private fun showUpdateSavedDialog(goal: SavingsGoal) {
        val input = android.widget.EditText(requireContext()).apply {
            hint = "Amount saved so far"
            inputType = android.text.InputType.TYPE_CLASS_NUMBER or
                    android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
            setText(goal.savedAmount.toInt().toString())
            setPadding(48, 32, 48, 16)
        }

        android.app.AlertDialog.Builder(requireContext())
            .setTitle("${goal.emoji} ${goal.name}")
            .setMessage("Update how much you've saved toward this goal")
            .setView(input)
            .setPositiveButton("Update") { _, _ ->
                val newAmount = input.text.toString().toDoubleOrNull() ?: goal.savedAmount
                viewModel.updateGoal(goal.copy(savedAmount = newAmount))
                Toast.makeText(requireContext(), "Goal updated! 🎯", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}