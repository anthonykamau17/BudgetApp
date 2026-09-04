package com.anthonykamau.budgetapp.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class SettingsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val text = TextView(requireContext())
        text.text = "Settings — coming soon"
        text.setTextColor(0xFFFFFFFF.toInt())
        return text
    }
}