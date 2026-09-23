package com.anthonykamau.budgetapp.ui.aicoach

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.anthonykamau.budgetapp.R
import com.anthonykamau.budgetapp.data.entity.ChatMessage

class ChatAdapter : ListAdapter<ChatMessage, ChatAdapter.MessageViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val llRoot = itemView.findViewById<LinearLayout>(R.id.ll_message_root)
        private val tvAvatar = itemView.findViewById<TextView>(R.id.tv_avatar)
        private val cardMessage = itemView.findViewById<CardView>(R.id.card_message)
        private val tvMessage = itemView.findViewById<TextView>(R.id.tv_message)

        fun bind(message: ChatMessage) {
            tvMessage.text = message.content

            if (message.isUser) {
                // user message — right aligned, orange background
                llRoot.gravity = android.view.Gravity.END
                tvAvatar.visibility = View.GONE
                cardMessage.setCardBackgroundColor(0xFFF59E0B.toInt())
                tvMessage.setTextColor(0xFF1A0800.toInt())
            } else {
                // AI message — left aligned, dark card background
                llRoot.gravity = android.view.Gravity.START
                tvAvatar.visibility = View.VISIBLE
                cardMessage.setCardBackgroundColor(0xFF14141C.toInt())
                tvMessage.setTextColor(0xFFFFFFFF.toInt())
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ChatMessage>() {
        override fun areItemsTheSame(oldItem: ChatMessage, newItem: ChatMessage) =
            oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: ChatMessage, newItem: ChatMessage) =
            oldItem == newItem
    }
}
