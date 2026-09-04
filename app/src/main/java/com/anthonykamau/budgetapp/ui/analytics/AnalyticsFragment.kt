package com.anthonykamau.budgetapp.ui.analytics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class AnalyticsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val text = TextView(requireContext())
        text.text = "Analytics — coming soon"
        text.setTextColor(0xFFFFFFFF.toInt())
        return text
    }
}