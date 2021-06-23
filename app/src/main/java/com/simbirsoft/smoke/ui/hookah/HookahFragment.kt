package com.simbirsoft.smoke.ui.hookah

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.simbirsoft.smoke.R
import com.simbirsoft.smoke.databinding.FragmentHookahBinding
import com.simbirsoft.smoke.databinding.ItemHookahBinding
import com.simbirsoft.smoke.domain.Hookah
import com.simbirsoft.smoke.presentation.HookahViewModel
import com.simbirsoft.smoke.ui.BaseFragment
import kotlinx.coroutines.flow.collectLatest

class HookahFragment : BaseFragment(R.layout.fragment_hookah) {
    private val binding by viewBinding<FragmentHookahBinding>()
    private val viewModel by viewModels<HookahViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = HookahAdapter().apply {
            setOnClickListener {
                findNavController().navigate(HookahFragmentDirections.toDetails())
            }
        }
        binding.recycler.adapter = adapter
        lifecycleScope.launchWhenStarted {
            viewModel.hookahs.collectLatest {
                adapter.submitData(it)
            }
        }
    }
}

class HookahAdapter() :
    PagingDataAdapter<Hookah, HookahAdapter.HookahViewHolder>(diffCallback) {
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
        holder.binding.hookahName.text = item.name
    }

    class HookahViewHolder(val binding: ItemHookahBinding) : RecyclerView.ViewHolder(binding.root)
}