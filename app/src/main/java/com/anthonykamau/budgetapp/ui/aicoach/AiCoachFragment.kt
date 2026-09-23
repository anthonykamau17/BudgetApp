package com.anthonykamau.budgetapp.ui.aicoach

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anthonykamau.budgetapp.R

class AiCoachFragment : Fragment() {

    private val viewModel: AiCoachViewModel by viewModels()
    private lateinit var adapter: ChatAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_ai_coach, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_chat)
        val etMessage = view.findViewById<EditText>(R.id.et_message)
        val btnSend = view.findViewById<CardView>(R.id.btn_send)
        val llLoading = view.findViewById<LinearLayout>(R.id.ll_loading)
        val tvBack = view.findViewById<TextView>(R.id.tv_back_coach)

        // back button
        tvBack.setOnClickListener { findNavController().navigateUp() }

        // set up chat adapter
        adapter = ChatAdapter()
        recyclerView.layoutManager = LinearLayoutManager(requireContext()).apply {
            stackFromEnd = true
        }
        recyclerView.adapter = adapter

        // observe messages
        viewModel.messages.observe(viewLifecycleOwner) { messages ->
            adapter.submitList(messages.toList())
            recyclerView.scrollToPosition(messages.size - 1)
        }

        // observe loading state
        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            llLoading.visibility = if (loading) View.VISIBLE else View.GONE
            btnSend.isEnabled = !loading
        }

        // send button
        btnSend.setOnClickListener {
            val message = etMessage.text.toString().trim()
            if (message.isNotEmpty()) {
                viewModel.sendMessage(message)
                etMessage.setText("")
            }
        }

        // show welcome message
        viewModel.initIfEmpty()
    }
}