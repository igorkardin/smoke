package com.simbirsoft.smoke.ui.shops

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.simbirsoft.smoke.databinding.ItemPromotionBinding
import com.simbirsoft.smoke.databinding.ItemShopBinding
import com.simbirsoft.smoke.domain.Discount
import com.simbirsoft.smoke.domain.Shop

class PromotionsAdapter : PagingDataAdapter<Discount, PromotionsAdapter.DiscountViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiscountViewHolder {
        return DiscountViewHolder(
            ItemPromotionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: DiscountViewHolder, position: Int) {
        val item = getItem(position)
        item ?: return
        holder.binding.apply {
            promotionName.text = item.name
        }
    }

    class DiscountViewHolder(val binding: ItemPromotionBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<Discount>() {
            override fun areItemsTheSame(oldItem: Discount, newItem: Discount) =
                areContentsTheSame(oldItem, newItem)

            override fun areContentsTheSame(oldItem: Discount, newItem: Discount) =
                oldItem == newItem
        }
    }
}