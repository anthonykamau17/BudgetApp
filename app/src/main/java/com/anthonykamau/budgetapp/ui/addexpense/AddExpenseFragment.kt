package com.anthonykamau.budgetapp.ui.addexpense

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.anthonykamau.budgetapp.R
import androidx.appcompat.widget.LinearLayoutCompat

class AddExpenseFragment : Fragment() {

    private val viewModel: AddExpenseViewModel by viewModels()

    // categories list
    private val categories = listOf(
        Triple(1, "Food", "🍔"),
        Triple(2, "Transport", "🚌"),
        Triple(3, "Books", "📚"),
        Triple(4, "Coffee", "☕"),
        Triple(5, "Rent", "🏠"),
        Triple(6, "Health", "💊"),
        Triple(7, "Fun", "🎮"),
        Triple(8, "Other", "➕")
    )

    // keypad keys
    private val keypadKeys = listOf(
        "1", "2", "3",
        "4", "5", "6",
        "7", "8", "9",
        ".", "0", "⌫"
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_add_expense, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvAmount = view.findViewById<TextView>(R.id.tv_amount_display)
        val tvCategory = view.findViewById<TextView>(R.id.tv_selected_category)
        val etNote = view.findViewById<EditText>(R.id.et_note)
        val btnSave = view.findViewById<TextView>(R.id.btn_save)
        val tvBack = view.findViewById<TextView>(R.id.tv_back)

        // back button
        tvBack.setOnClickListener { findNavController().navigateUp() }

        // observe amount input
        viewModel.amountInput.observe(viewLifecycleOwner) { amount ->
            tvAmount.text = amount
        }

        // observe selected category
        viewModel.selectedCategoryName.observe(viewLifecycleOwner) { name ->
            tvCategory.text = if (name.isEmpty()) "tap a category below" else name
        }

        // build category grid
        buildCategoryGrid(view)

        // pre-select category if passed from Quick Add
        val preSelectedCategory = arguments?.getString("categoryName") ?: ""
        if (preSelectedCategory.isNotEmpty()) {
            val match = categories.find { it.second == preSelectedCategory }
            match?.let { (id, name, emoji) ->
                viewModel.selectCategory(id, name, emoji)
                // highlight the matching button in the grid
                val grid = view.findViewById<GridLayout>(R.id.grid_categories)
                val index = categories.indexOf(match)
                if (index >= 0 && index < grid.childCount) {
                    (grid.getChildAt(index) as? androidx.cardview.widget.CardView)
                        ?.setCardBackgroundColor(resources.getColor(R.color.bg_deep, null))
                }
            }
        }

        // build keypad
        buildKeypad(view)

        // save button
        btnSave.setOnClickListener {
            val note = etNote.text.toString().trim()
            viewModel.saveExpense(note)
        }

        // observe save result
        viewModel.saveResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                SaveResult.SUCCESS -> {
                    Toast.makeText(requireContext(), "Expense saved! 💰", Toast.LENGTH_SHORT).show()
                    findNavController().navigateUp()
                }
                SaveResult.ERROR_AMOUNT -> {
                    Toast.makeText(requireContext(), "Enter an amount first", Toast.LENGTH_SHORT).show()
                }
                SaveResult.ERROR_CATEGORY -> {
                    Toast.makeText(requireContext(), "Select a category", Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }
    }

    private fun buildCategoryGrid(view: View) {
        val grid = view.findViewById<GridLayout>(R.id.grid_categories)
        grid.removeAllViews()

        val screenWidth = resources.displayMetrics.widthPixels
        val margin = (16 * resources.displayMetrics.density).toInt()
        val cellSize = (screenWidth - margin * 2) / 4

        categories.forEach { (id, name, emoji) ->
            val card = CardView(requireContext()).apply {
                radius = 12f * resources.displayMetrics.density
                setCardBackgroundColor(resources.getColor(R.color.bg_card, null))
                cardElevation = 0f
                val params = GridLayout.LayoutParams().apply {
                    width = cellSize - 8
                    height = GridLayout.LayoutParams.WRAP_CONTENT
                    setMargins(4, 4, 4, 4)
                }
                layoutParams = params
            }

            val inner = LinearLayoutCompat(requireContext()).apply {
                orientation = LinearLayoutCompat.VERTICAL
                gravity = android.view.Gravity.CENTER
                setPadding(8, 12, 8, 12)
            }

            val tvEmoji = TextView(requireContext()).apply {
                text = emoji
                textSize = 22f
                gravity = android.view.Gravity.CENTER
            }

            val tvName = TextView(requireContext()).apply {
                text = name
                textSize = 10f
                gravity = android.view.Gravity.CENTER
                setTextColor(resources.getColor(R.color.text_secondary, null))
                setPadding(0, 4, 0, 0)
            }

            inner.addView(tvEmoji)
            inner.addView(tvName)
            card.addView(inner)

            card.setOnClickListener {
                viewModel.selectCategory(id, name, emoji)
                // highlight selected
                for (i in 0 until grid.childCount) {
                    (grid.getChildAt(i) as? CardView)?.setCardBackgroundColor(
                        resources.getColor(R.color.bg_card, null)
                    )
                }
                card.setCardBackgroundColor(
                    resources.getColor(R.color.bg_deep, null)
                )
            }

            grid.addView(card)
        }
    }

    private fun buildKeypad(view: View) {
        val grid = view.findViewById<GridLayout>(R.id.grid_keypad)
        grid.removeAllViews()

        val screenWidth = resources.displayMetrics.widthPixels
        val margin = (16 * resources.displayMetrics.density).toInt()
        val cellSize = (screenWidth - margin * 2) / 3

        keypadKeys.forEach { key ->
            val card = CardView(requireContext()).apply {
                radius = 10f * resources.displayMetrics.density
                setCardBackgroundColor(resources.getColor(R.color.bg_card, null))
                cardElevation = 0f
                val params = GridLayout.LayoutParams().apply {
                    width = cellSize - 8
                    height = (cellSize * 0.6).toInt()
                    setMargins(4, 4, 4, 4)
                }
                layoutParams = params
            }

            val tvKey = TextView(requireContext()).apply {
                text = key
                textSize = if (key == "⌫") 18f else 22f
                gravity = android.view.Gravity.CENTER
                setTextColor(
                    if (key == "⌫")
                        resources.getColor(R.color.orange_bright, null)
                    else
                        resources.getColor(R.color.text_primary, null)
                )
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }

            card.addView(tvKey)
            card.setOnClickListener { viewModel.onKeyPressed(key) }
            grid.addView(card)
        }
    }
}