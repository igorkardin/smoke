package com.simbirsoft.smoke.ui.video

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.simbirsoft.smoke.databinding.ItemVideoBinding
import com.simbirsoft.smoke.domain.Video

class VideoAdapter : PagingDataAdapter<Video, VideoAdapter.VideoViewHolder>(diffCallback) {
    private var clickListener: ((Video) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        return VideoViewHolder(
            ItemVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
            .apply {
                itemView.setOnClickListener {
                    val item = getItem(absoluteAdapterPosition) ?: return@setOnClickListener
                    clickListener?.invoke(item)
                }
            }
    }

    fun setOnClickListener(listener: ((Video) -> Unit)?) {
        clickListener = listener
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val item = getItem(position)
        item ?: return
        holder.binding.apply {
            title.text = item.title
            duration.text = item.length
            Glide.with(holder.itemView)
                .load(item.preview)
                .into(holder.binding.videoImage)
        }
    }

    class VideoViewHolder(val binding: ItemVideoBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<Video>() {
            override fun areItemsTheSame(oldItem: Video, newItem: Video) =
                areContentsTheSame(oldItem, newItem)

            override fun areContentsTheSame(oldItem: Video, newItem: Video) = oldItem == newItem
        }
    }
}