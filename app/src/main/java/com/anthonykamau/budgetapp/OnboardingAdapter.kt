package com.anthonykamau.budgetapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class OnboardingSlide(
    val emoji: String,
    val title: String,
    val description: String,
    val tag: String = ""
)

class OnboardingAdapter(
    private val slides: List<OnboardingSlide>
) : RecyclerView.Adapter<OnboardingAdapter.SlideViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlideViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_onboarding_slide, parent, false)
        return SlideViewHolder(view)
    }

    override fun onBindViewHolder(holder: SlideViewHolder, position: Int) {
        holder.bind(slides[position])
    }

    override fun getItemCount() = slides.size

    class SlideViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvEmoji = itemView.findViewById<TextView>(R.id.tv_slide_emoji)
        private val tvTitle = itemView.findViewById<TextView>(R.id.tv_slide_title)
        private val tvDesc = itemView.findViewById<TextView>(R.id.tv_slide_desc)
        private val tvTag = itemView.findViewById<TextView>(R.id.tv_slide_tag)

        fun bind(slide: OnboardingSlide) {
            tvEmoji.text = slide.emoji
            tvTitle.text = slide.title
            tvDesc.text = slide.description
            if (slide.tag.isNotEmpty()) {
                tvTag.text = slide.tag
                tvTag.visibility = View.VISIBLE
            } else {
                tvTag.visibility = View.GONE
            }
        }
    }
}