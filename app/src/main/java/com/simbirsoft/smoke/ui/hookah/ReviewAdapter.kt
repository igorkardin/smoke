package com.simbirsoft.smoke.ui.hookah

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.simbirsoft.smoke.databinding.ItemRatingBinding
import com.simbirsoft.smoke.domain.Review

class ReviewAdapter : PagingDataAdapter<Review, ReviewAdapter.ReviewViewHolder>(diffCallback) {
    private var clickListener: ((Review) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        return ReviewViewHolder(ItemRatingBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            .apply {
                itemView.setOnClickListener {
                    val item = getItem(absoluteAdapterPosition) ?: return@setOnClickListener
                    clickListener?.invoke(item)
                }
            }
    }

    fun setOnClickListener(listener: ((Review) -> Unit)?) {
        clickListener = listener
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val item = getItem(position)
        item ?: return
        holder.binding.apply {
            description.text = item.body
        }
    }

    class ReviewViewHolder(val binding: ItemRatingBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<Review>() {
            override fun areItemsTheSame(oldItem: Review, newItem: Review) = areContentsTheSame(oldItem, newItem)
            override fun areContentsTheSame(oldItem: Review, newItem: Review) = oldItem == newItem
        }
    }
}