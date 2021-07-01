package com.simbirsoft.smoke.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.simbirsoft.smoke.databinding.LoadStateViewBinding

class BaseLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<BaseLoadStateAdapter.LoadStateViewHolder>() {

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.binding.loadStateRetry.isVisible = loadState !is LoadState.Loading
        holder.binding.loadStateErrorMessage.isVisible = loadState !is LoadState.Loading
        holder.binding.loadStateProgress.isVisible = loadState is LoadState.Loading
        Log.e("State", loadState.toString())

        if (loadState is LoadState.Error) {
            holder.binding.loadStateErrorMessage.text = loadState.error.localizedMessage
        }
        holder.binding.loadStateRetry.setOnClickListener {
            retry.invoke()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val binding = LoadStateViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoadStateViewHolder(binding)
    }

    class LoadStateViewHolder(val binding: LoadStateViewBinding) : RecyclerView.ViewHolder(binding.root)
}