package com.simbirsoft.smoke.ui.hookah

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.simbirsoft.smoke.R
import com.simbirsoft.smoke.databinding.ItemHookahBinding
import com.simbirsoft.smoke.domain.Hookah

class HookahAdapter : PagingDataAdapter<Hookah, HookahAdapter.HookahViewHolder>(diffCallback) {
    private var clickListener: ((Hookah) -> Unit)? = null

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<Hookah>() {
            override fun areItemsTheSame(oldItem: Hookah, newItem: Hookah) = areContentsTheSame(oldItem, newItem)
            override fun areContentsTheSame(oldItem: Hookah, newItem: Hookah) = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HookahViewHolder {
        return HookahViewHolder(ItemHookahBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            .apply {
                itemView.setOnClickListener {
                    val item = getItem(absoluteAdapterPosition) ?: return@setOnClickListener
                    clickListener?.invoke(item)
                }
            }
    }

    fun setOnClickListener(listener: ((Hookah) -> Unit)?) {
        clickListener = listener
    }

    override fun onBindViewHolder(holder: HookahViewHolder, position: Int) {
        val item = getItem(position)
        item ?: return
        Glide.with(holder.itemView)
            .load(item.picture)
            .into(holder.binding.hookahImage)
        holder.binding.apply {
            hookahName.text = item.name
            description.text = item.description
            rating.text = root.context.resources.getString(R.string.rating, item.rating.average)
            hookahPrice.text = root.context.resources.getString(R.string.price, item.price)
        }
    }

    class HookahViewHolder(val binding: ItemHookahBinding) : RecyclerView.ViewHolder(binding.root)
}