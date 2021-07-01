package com.simbirsoft.smoke.ui.hookah

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.simbirsoft.smoke.App
import com.simbirsoft.smoke.R
import com.simbirsoft.smoke.databinding.FragmentHookahBinding
import com.simbirsoft.smoke.presentation.HookahViewModel
import com.simbirsoft.smoke.ui.BaseFragment
import com.simbirsoft.smoke.ui.BaseLoadStateAdapter
import com.simbirsoft.smoke.ui.main.BottomNavFragmentDirections
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class HookahFragment : BaseFragment(R.layout.fragment_hookah) {

    @Inject
    lateinit var viewModelFactory: HookahViewModel.Factory

    private lateinit var binding: FragmentHookahBinding
    private val viewModel by viewModels<HookahViewModel> { viewModelFactory }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHookahBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun inject(app: App) = app.hookahComponent?.inject(this) ?: Unit

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val hookahAdapter = HookahAdapter().apply {
            setOnClickListener {
                mainNavController.navigate(BottomNavFragmentDirections.toHookahDetails(it))
            }
        }
        binding.recycler.adapter = hookahAdapter.withLoadStateFooter(
            footer = BaseLoadStateAdapter { hookahAdapter.retry() }
        )
        lifecycleScope.launchWhenStarted {
            viewModel.hookahs.collectLatest { hookahAdapter.submitData(it) }
        }
    }
}