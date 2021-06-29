package com.simbirsoft.smoke.ui.shops

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.simbirsoft.smoke.databinding.ItemShopBinding
import com.simbirsoft.smoke.domain.Shop

class ShopsAdapter : PagingDataAdapter<Shop, ShopsAdapter.ShopsViewHolder>(diffCallback) {
    private var clickListener: ((Shop) -> Unit)? = null
    private var clickOpenMapListener: ((Shop) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopsViewHolder {
        return ShopsViewHolder(
            ItemShopBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
            .apply {
                itemView.setOnClickListener {
                    val item = getItem(absoluteAdapterPosition) ?: return@setOnClickListener
                    clickListener?.invoke(item)
                }
                binding.map.setOnClickListener {
                    val item = getItem(absoluteAdapterPosition) ?: return@setOnClickListener
                    clickOpenMapListener?.invoke(item)
                }
            }
    }

    fun setOnClickListener(listener: ((Shop) -> Unit)?) {
        clickListener = listener
    }

    fun setOnClickMapListener(listener: ((Shop) -> Unit)?) {
        clickOpenMapListener = listener
    }

    override fun onBindViewHolder(holder: ShopsViewHolder, position: Int) {
        val item = getItem(position)
        item ?: return
        holder.binding.apply {
            shopName.text = item.name
        }
    }

    class ShopsViewHolder(val binding: ItemShopBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<Shop>() {
            override fun areItemsTheSame(oldItem: Shop, newItem: Shop) =
                areContentsTheSame(oldItem, newItem)

            override fun areContentsTheSame(oldItem: Shop, newItem: Shop) = oldItem == newItem
        }
    }
}