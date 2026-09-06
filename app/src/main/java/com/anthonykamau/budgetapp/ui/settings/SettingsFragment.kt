package com.anthonykamau.budgetapp.ui.settings

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.anthonykamau.budgetapp.R

class SettingsFragment : Fragment() {

    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etName = view.findViewById<EditText>(R.id.et_user_name)
        val etBudget = view.findViewById<EditText>(R.id.et_monthly_budget)
        val etCurrency = view.findViewById<EditText>(R.id.et_currency)
        val switch80 = view.findViewById<Switch>(R.id.switch_notify_80)
        val switch100 = view.findViewById<Switch>(R.id.switch_notify_100)
        val btnSave = view.findViewById<TextView>(R.id.btn_save_settings)
        val btnReset = view.findViewById<TextView>(R.id.btn_reset_month)
        val btnClear = view.findViewById<TextView>(R.id.btn_clear_all)

        // load existing settings into fields
        viewModel.settings.observe(viewLifecycleOwner) { settings ->
            if (settings != null) {
                etName.setText(settings.userName)
                etBudget.setText(
                    if (settings.monthlyBudget > 0)
                        settings.monthlyBudget.toInt().toString()
                    else ""
                )
                etCurrency.setText(settings.currency)
                switch80.isChecked = settings.notifyAt80
                switch100.isChecked = settings.notifyAt100
            }
        }

        // save button
        btnSave.setOnClickListener {
            val name = etName.text.toString()
            val budgetStr = etBudget.text.toString()
            val currency = etCurrency.text.toString()

            if (budgetStr.isEmpty()) {
                Toast.makeText(requireContext(), "Enter your monthly budget", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val budget = budgetStr.toDoubleOrNull() ?: 0.0
            if (budget <= 0) {
                Toast.makeText(requireContext(), "Budget must be greater than 0", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.saveSettings(
                userName = name,
                monthlyBudget = budget,
                currency = currency,
                notifyAt80 = switch80.isChecked,
                notifyAt100 = switch100.isChecked
            )
        }

        // observe save result
        viewModel.saveResult.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(requireContext(), "Settings saved! ✅", Toast.LENGTH_SHORT).show()
            }
        }

        // reset month button
        // reset month button
        btnReset.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Reset this month?")
                .setMessage("This will delete all expenses from this month. Are you sure?")
                .setPositiveButton("Reset") { _, _ ->
                    viewModel.resetMonthExpenses()
                    Toast.makeText(requireContext(), "This month's expenses cleared ✅", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }

// clear all data button
        btnClear.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Clear all data?")
                .setMessage("This will permanently delete ALL your expenses and goals. This cannot be undone.")
                .setPositiveButton("Delete everything") { _, _ ->
                    AlertDialog.Builder(requireContext())
                        .setTitle("Are you absolutely sure?")
                        .setMessage("Everything will be gone. For real.")
                        .setPositiveButton("Yes, delete") { _, _ ->
                            viewModel.clearAllData()
                            Toast.makeText(requireContext(), "All data cleared 🗑️", Toast.LENGTH_SHORT).show()
                        }
                        .setNegativeButton("Cancel", null)
                        .show()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }

    }
}